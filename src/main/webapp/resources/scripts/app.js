(() => {
    const form = document.getElementById("hit-form");
    const yInput = document.getElementById("y-input");
    const errorsBox = document.getElementById("client-errors");
    const errorsList = errorsBox ? errorsBox.querySelector("ul") : null;
    const plot = document.querySelector("[data-plot]");
    const selectionCircle = document.getElementById("selected-point");
    const selectionText = document.getElementById("selection-readout");

    const allowedX = new Set(["-4", "-3", "-2", "-1", "0", "1", "2", "3", "4"]);
    const allowedR = new Set(["1", "2", "3", "4", "5"]);
    const Y_MIN = -5;
    const Y_MAX = 3;
    const BOARD_RADIUS = 120;

    updateSelectionFromForm();
    formatTimestamps();

    form?.addEventListener("submit", (event) => {
        const issues = validateForm();
        if (issues.length > 0) {
            event.preventDefault();
            showErrors(issues);
            return;
        }
        hideErrors();
    });

    if (yInput) {
        yInput.addEventListener("input", () => {
            const sanitized = yInput.value.replace(/[^0-9+\-.,]/g, "").replace(",", ".");
            if (sanitized !== yInput.value) {
                yInput.value = sanitized;
            }
            updateSelectionFromForm();
        });
    }

    document.querySelectorAll('input[name="x"]').forEach((input) => {
        input.addEventListener("change", updateSelectionFromForm);
    });

    document.querySelectorAll('input[name="r"]').forEach((input) => {
        input.addEventListener("change", updateSelectionFromForm);
    });

    if (plot && form) {
        plot.addEventListener("click", (event) => {
            const radiusValue = getSelectedValue("r");
            if (!radiusValue) {
                showErrors(["Select radius R before clicking the plot."]);
                return;
            }

            const pointer = toPlotCoordinates(plot, event);
            if (!pointer) {
                return;
            }

            const radius = Number.parseFloat(radiusValue);
            const xCandidate = clamp((pointer.x / BOARD_RADIUS) * radius, -4, 4);
            const yCandidate = clamp((-pointer.y / BOARD_RADIUS) * radius, Y_MIN, Y_MAX);

            const snappedX = snapToAllowed(xCandidate);
            setRadioValue("x", snappedX);
            if (yInput) {
                yInput.value = yCandidate.toString();
            }

            hideErrors();
            updateSelectionFromForm();
        });
    }

    function validateForm() {
        const messages = [];
        const xValue = getSelectedValue("x");
        const rValue = getSelectedValue("r");
        const yRaw = yInput ? yInput.value.trim().replace(",", ".") : "";

        if (!xValue || !allowedX.has(xValue)) {
            messages.push("Pick a value for X.");
        }

        if (yRaw === null || yRaw === "") {
            messages.push("Enter a numeric value for Y.");
        } else if (!/^[-+]?\d+(\.\d+)?$/.test(yRaw)) {
            messages.push("Y must be a valid number.");
        } else {
            const yValue = Number.parseFloat(yRaw);
            if (!Number.isFinite(yValue)) {
                messages.push("Y must be a valid number.");
            } else if (yValue < Y_MIN || yValue > Y_MAX) {
                messages.push(`Y must be within the range [${Y_MIN}, ${Y_MAX}].`);
            }
        }

        if (!rValue || !allowedR.has(rValue)) {
            messages.push("Pick a value for R.");
        }

        return messages;
    }

    function updateSelectionFromForm() {
        const xValue = getSelectedValue("x");
        const rValue = getSelectedValue("r");
        const yRaw = yInput ? yInput.value.trim().replace(",", ".") : "";
        const yValue = yRaw === "" ? NaN : Number.parseFloat(yRaw);

        if (!selectionCircle || !selectionText || !xValue || !rValue || !Number.isFinite(yValue)) {
            clearSelection();
            return;
        }

        const scale = BOARD_RADIUS / Number.parseFloat(rValue);
        selectionCircle.setAttribute("cx", (Number.parseFloat(xValue) * scale).toFixed(2));
        selectionCircle.setAttribute("cy", (-yValue * scale).toFixed(2));
        selectionCircle.removeAttribute("hidden");
        selectionText.textContent = `Selected: X = ${xValue}, Y = ${yRaw}, R = ${formatNumber(rValue)}`;
    }

    function clearSelection() {
        if (selectionCircle) {
            selectionCircle.setAttribute("hidden", "hidden");
        }
        if (selectionText) {
            selectionText.textContent = "Selected: none";
        }
    }

    function showErrors(items) {
        if (!errorsBox || !errorsList) {
            return;
        }
        errorsList.replaceChildren();
        items.forEach((message) => {
            const li = document.createElement("li");
            li.textContent = message;
            errorsList.appendChild(li);
        });
        errorsBox.hidden = false;
        errorsBox.focus();
    }

    function hideErrors() {
        if (!errorsBox || !errorsList) {
            return;
        }
        errorsList.replaceChildren();
        errorsBox.hidden = true;
    }

    function getSelectedValue(name) {
        const control = document.querySelector(`input[name="${CSS.escape(name)}"]:checked`);
        return control ? control.value : null;
    }

    function setRadioValue(name, value) {
        const control = document.querySelector(`input[name="${CSS.escape(name)}"][value="${CSS.escape(value)}"]`);
        if (control) {
            control.checked = true;
        }
    }

    function snapToAllowed(candidate) {
        const values = [-4, -3, -2, -1, 0, 1, 2, 3, 4];
        let closest = values[0];
        let minDelta = Math.abs(candidate - closest);
        for (let i = 1; i < values.length; i += 1) {
            const delta = Math.abs(candidate - values[i]);
            if (delta < minDelta) {
                closest = values[i];
                minDelta = delta;
            }
        }
        return String(closest);
    }

    function clamp(value, min, max) {
        return Math.min(Math.max(value, min), max);
    }

    function formatNumber(value) {
        return Number.parseFloat(Number(value).toFixed(3)).toString();
    }

    function toPlotCoordinates(svgElement, event) {
        const point = svgElement.createSVGPoint();
        point.x = event.clientX;
        point.y = event.clientY;
        const ctm = svgElement.getScreenCTM();
        if (!ctm) {
            return null;
        }
        return point.matrixTransform(ctm.inverse());
    }

    function formatTimestamps() {
        document.querySelectorAll('.timestamp').forEach(element => {
            const timestamp = element.getAttribute('data-timestamp');
            if (timestamp) {
                try {
                    const date = new Date(timestamp);
                    const formatted = date.toLocaleString();
                    element.textContent = formatted;
                } catch (e) {
                }
            }
        });
    }
})();
