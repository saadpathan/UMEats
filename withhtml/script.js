document.addEventListener('DOMContentLoaded', () => {
    const meals = new Map();
    let servedMeals = 0;

    const areas = [
        "Kolej Kediaman 01", "Kolej Kediaman 02", "Kolej Kediaman 03",
        "Kolej Kediaman 04", "Kolej Kediaman 05", "Kolej Kediaman 06",
        "Kolej Kediaman 07", "Kolej Kediaman 08", "Kolej Kediaman 09",
        "Kolej Kediaman 10", "Kolej Kediaman 11", "Kolej Kediaman 12",
        "Kolej Kediaman 13"
    ];
    const blocks = ["Block A", "Block B", "Block C", "Block D"];

    const availableMealsElement = document.getElementById('availableMeals');
    const servedMealsElement = document.getElementById('servedMeals');
    const addMealBtn = document.getElementById('addMealBtn');
    const takeMealBtn = document.getElementById('takeMealBtn');
    const exitBtn = document.getElementById('exitBtn');
    const formSection = document.getElementById('formSection');
    const mealForm = document.getElementById('mealForm');
    const areaSelect = document.getElementById('area');
    const blockSelect = document.getElementById('block');
    const quantityInput = document.getElementById('quantity');
    const messageElement = document.getElementById('message');

    function updateMealCount(area, block, quantity) {
        if (!meals.has(area)) {
            meals.set(area, new Map());
        }
        const areaMap = meals.get(area);
        const currentQuantity = areaMap.get(block) || 0;
        areaMap.set(block, currentQuantity + quantity);
    }

    function displayStatistics() {
        let totalAvailableMeals = 0;
        meals.forEach(area => {
            area.forEach(quantity => {
                totalAvailableMeals += quantity;
            });
        });
        availableMealsElement.textContent = totalAvailableMeals;
        servedMealsElement.textContent = servedMeals;
    }

    function resetForm() {
        areaSelect.value = '';
        blockSelect.value = '';
        quantityInput.value = '';
    }

    function showForm() {
        formSection.classList.remove('hidden');
        messageElement.classList.add('hidden');
    }

    function hideForm() {
        formSection.classList.add('hidden');
    }

    function showMessage(message) {
        messageElement.textContent = message;
        messageElement.classList.remove('hidden');
    }

    areas.forEach(area => {
        const option = document.createElement('option');
        option.value = area.toUpperCase();
        option.textContent = area;
        areaSelect.appendChild(option);
    });

    blocks.forEach(block => {
        const option = document.createElement('option');
        option.value = block.toUpperCase();
        option.textContent = block;
        blockSelect.appendChild(option);
    });

    addMealBtn.addEventListener('click', () => {
        showForm();
        mealForm.onsubmit = (e) => {
            e.preventDefault();
            const area = areaSelect.value;
            const block = blockSelect.value;
            const quantity = parseInt(quantityInput.value, 10);

            if (!area || !block || isNaN(quantity)) {
                showMessage("Invalid input. Please enter valid details.");
                return;
            }

            updateMealCount(area, block, quantity);
            showMessage("Your Meal Added Successfully");
            displayStatistics();
            resetForm();
        };
    });

    takeMealBtn.addEventListener('click', () => {
        showForm();
        mealForm.onsubmit = (e) => {
            e.preventDefault();
            const area = areaSelect.value;
            const block = blockSelect.value;
            const quantity = parseInt(quantityInput.value, 10);

            if (!area || !block || isNaN(quantity) || quantity < 1 || quantity > 4) {
                showMessage("Invalid quantity. Please select between 1 and 4.");
                return;
            }

            const availableMeals = meals.get(area)?.get(block) || 0;
            if (availableMeals < quantity) {
                showMessage(`Not enough meals available in Block ${block}.`);
                return;
            }

            updateMealCount(area, block, -quantity);
            servedMeals += quantity;
            showMessage("Your Meal Is Ready.");
            displayStatistics();
            resetForm();
        };
    });

    exitBtn.addEventListener('click', () => {
        showMessage("Thank You For Sharing Your Meal With UMEats. Goodbye!");
        hideForm();
    });

    displayStatistics();
});
