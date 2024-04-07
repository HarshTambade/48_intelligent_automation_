// Firebase configuration
const firebaseConfig = {
    apiKey: "YOUR_API_KEY",
    authDomain: "YOUR_AUTH_DOMAIN",
    databaseURL: "YOUR_DATABASE_URL",
    projectId: "YOUR_PROJECT_ID",
    storageBucket: "YOUR_STORAGE_BUCKET",
    messagingSenderId: "YOUR_MESSAGING_SENDER_ID",
    appId: "YOUR_APP_ID"
};

// Initialize Firebase
firebase.initializeApp(firebaseConfig);

// Get a reference to the database service
const database = firebase.database();

function processUserChoice() {
    const userInput = document.getElementById("userInput").value.trim();
    const chatMessages = document.getElementById("chat-messages");

    // Display user's choice
    const userChoiceMessage = document.createElement("div");
    userChoiceMessage.classList.add("message");
    userChoiceMessage.textContent = "You selected option " + userInput + ".";
    chatMessages.appendChild(userChoiceMessage);

    // Handle user's choice
    switch(userInput) {
        case '1':
            displayEmployeeOptions();
            break;
        case '2':
            // Code to handle option 2 (Attendance)
            break;
        case '3':
            // Code to handle option 3 (Leave Management)
            break;
        case '4':
            // Code to handle option 4 (Performance)
            break;
        case '5':
            // Code to handle option 5 (Reports)
            break;
        default:
            // Handle invalid input
            const errorMessage = document.createElement("div");
            errorMessage.classList.add("message");
            errorMessage.textContent = "Invalid option. Please select a number from 1 to 5.";
            chatMessages.appendChild(errorMessage);
            break;
    }

    // Clear user input
    document.getElementById("userInput").value = "";
}

function displayEmployeeOptions() {
    const employeeOptionsMessage = document.createElement("div");
    employeeOptionsMessage.classList.add("message");
    employeeOptionsMessage.textContent = "Please select one of the following employee options:";
    chatMessages.appendChild(employeeOptionsMessage);

    const employeeOptions = [
        "View Employee",
        "Add Employee",
        "Delete Employee",
        "Edit Employee"
    ];

    employeeOptions.forEach(option => {
        const optionMessage = document.createElement("div");
        optionMessage.classList.add("message");
        optionMessage.textContent = option;
        chatMessages.appendChild(optionMessage);
    });
}

function addEmployee() {
    const employeeId = prompt("Enter employee ID:");
    const email = prompt("Enter email:");
    const firstName = prompt("Enter first name:");
    const lastName = prompt("Enter last name:");
    const phoneNo = prompt("Enter phone number:");

    if (employeeId && email && firstName && lastName && phoneNo) {
        database.ref('employees').push({
            employeeId: employeeId,
            email: email,
            firstName: firstName,
            lastName: lastName,
            phoneNo: phoneNo
        });

        const successMessage = document.createElement("div");
        successMessage.classList.add("message");
        successMessage.textContent = "Employee added successfully!";
        chatMessages.appendChild(successMessage);
    } else {
        const errorMessage = document.createElement("div");
        errorMessage.classList.add("message");
        errorMessage.textContent = "Please fill in all fields.";
        chatMessages.appendChild(errorMessage);
    }
}

function deleteEmployee() {
    const employeeId = prompt("Enter employee ID to delete:");
    
    if (employeeId) {
        database.ref('employees').orderByChild('employeeId').equalTo(employeeId).once('value', snapshot => {
            if (snapshot.exists()) {
                snapshot.forEach(childSnapshot => {
                    childSnapshot.ref.remove();
                });
                const successMessage = document.createElement("div");
                successMessage.classList.add("message");
                successMessage.textContent = "Employee deleted successfully!";
                chatMessages.appendChild(successMessage);
            } else {
                const errorMessage = document.createElement("div");
                errorMessage.classList.add("message");
                errorMessage.textContent = "Employee not found.";
                chatMessages.appendChild(errorMessage);
            }
        });
    } else {
        const errorMessage = document.createElement("div");
        errorMessage.classList.add("message");
        errorMessage.textContent = "Please enter an employee ID.";
        chatMessages.appendChild(errorMessage);
    }
}

function editEmployee() {
    const employeeId = prompt("Enter employee ID to edit:");
    
    if (employeeId) {
        const email = prompt("Enter new email:");
        const firstName = prompt("Enter new first name:");
        const lastName = prompt("Enter new last name:");
        const phoneNo = prompt("Enter new phone number:");

        if (email && firstName && lastName && phoneNo) {
            database.ref('employees').orderByChild('employeeId').equalTo(employeeId).once('value', snapshot => {
                if (snapshot.exists()) {
                    snapshot.forEach(childSnapshot => {
                        childSnapshot.ref.update({
                            email: email,
                            firstName: firstName,
                            lastName: lastName,
                            phoneNo: phoneNo
                        });
                    });
                    const successMessage = document.createElement("div");
                    successMessage.classList.add("message");
                    successMessage.textContent = "Employee details updated successfully!";
                    chatMessages.appendChild(successMessage);
                } else {
                    const errorMessage = document.createElement("div");
                    errorMessage.classList.add("message");
                    errorMessage.textContent = "Employee not found.";
                    chatMessages.appendChild(errorMessage);
                }
            });
        } else {
            const errorMessage = document.createElement("div");
            errorMessage.classList.add("message");
            errorMessage.textContent = "Please fill in all fields.";
            chatMessages.appendChild(errorMessage);
        }
    } else {
        const errorMessage = document.createElement("div");
        errorMessage.classList.add("message");
        errorMessage.textContent = "Please enter an employee ID.";
        chatMessages.appendChild(errorMessage);
    }
}
