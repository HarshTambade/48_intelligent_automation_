// Initialize Firebase
const firebaseConfig = {
    apiKey: "YOUR_API_KEY",
    authDomain: "YOUR_AUTH_DOMAIN",
    projectId: "YOUR_PROJECT_ID",
    storageBucket: "YOUR_STORAGE_BUCKET",
    messagingSenderId: "YOUR_MESSAGING_SENDER_ID",
    appId: "YOUR_APP_ID"
};

firebase.initializeApp(firebaseConfig);
const db = firebase.firestore();

// Function to add a new employee to Firestore
function addEmployee(employeeData) {
    db.collection("employees").add(employeeData)
    .then(function(docRef) {
        console.log("Employee added with ID: ", docRef.id);
    })
    .catch(function(error) {
        console.error("Error adding employee: ", error);
    });
}

// Function to display employees
function displayEmployees() {
    const employeeList = document.getElementById('employeeList');
    employeeList.innerHTML = '';
    db.collection("employees").get().then((querySnapshot) => {
        querySnapshot.forEach((doc) => {
            const employee = doc.data();
            const div = document.createElement('div');
            div.classList.add('employee');
            div.innerHTML = `
                <p><strong>Name:</strong> ${employee.firstName} ${employee.lastName}</p>
                <p><strong>Email:</strong> ${employee.email}</p>
                <p><strong>Phone:</strong> ${employee.phone}</p>
                <p><strong>Date of Birth:</strong> ${employee.dob}</p>
                <p><strong>Address:</strong> ${employee.address}</p>
                <!-- Display custom fields -->
                <p><strong>Custom Field 1:</strong> ${employee.customField1}</p>
                <p><strong>Custom Field 2:</strong> ${employee.customField2}</p>
            `;
            employeeList.appendChild(div);
        });
    });
}

// Event listener for Add Employee button
document.getElementById('addEmployeeBtn').addEventListener('click', () => {
    openModal('Add Employee');
});

// Event listener for Submit button in modal
document.getElementById('employeeForm').addEventListener('submit', (event) => {
    event.preventDefault();
    const form = event.target;
    const employeeData = {
        firstName: form.firstName.value,
        lastName: form.lastName.value,
        email: form.email.value,
        phone: form.phone.value,
        dob: form.dob.value,
        address: form.address.value,
        customField1: form.customField1.value,
        customField2: form.customField2.value
    };
    addEmployee(employeeData);
    closeModal();
});

// Event listener for closing modals
document.querySelectorAll('.close').forEach((closeBtn) => {
    closeBtn.addEventListener('click', () => {
        closeModal();
    });
});

// Function to open modal for adding/editing employee
function openModal(title, employeeIndex = null) {
    const modal = document.getElementById('employeeModal');
    const modalTitle = document.getElementById('modalTitle');
    const submitBtn = document.getElementById('submitBtn');
    const form = document.getElementById('employeeForm');

    modalTitle.innerText = title;
    modal.style.display = 'block';

    form.reset();

    submitBtn.innerText = title === 'Add Employee' ? 'Add Employee' : 'Update Employee';

    if (employeeIndex !== null) {
        const employee = employees[employeeIndex];
        form.firstName.value = employee.firstName;
        form.lastName.value = employee.lastName;
        form.email.value = employee.email;
        form.phone.value = employee.phone;
        form.dob.value = employee.dob;
        form.address.value = employee.address;
        form.customField1.value = employee.customField1;
        form.customField2.value = employee.customField2;
        submitBtn.setAttribute('data-index', employeeIndex);
    } else {
        submitBtn.removeAttribute('data-index');
    }
}

// Function to close modal
function closeModal() {
    const modals = document.querySelectorAll('.modal');
    modals.forEach((modal) => {
        modal.style.display = 'none';
    });
}

// Initialize display of employees
displayEmployees();
