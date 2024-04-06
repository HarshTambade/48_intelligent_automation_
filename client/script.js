// Open the modal when "Add Employee" button is clicked
const addEmployeeBtn = document.getElementById('addEmployeeBtn');
const addEmployeeModal = document.getElementById('addEmployeeModal');
const closeBtn = document.querySelector('.close');

addEmployeeBtn.addEventListener('click', () => {
  addEmployeeModal.style.display = 'block';
});

// Close the modal when close button is clicked
closeBtn.addEventListener('click', () => {
  addEmployeeModal.style.display = 'none';
});

// Close the modal when clicking outside of it
window.addEventListener('click', (event) => {
  if (event.target == addEmployeeModal) {
    addEmployeeModal.style.display = 'none';
  }
});

// Prevent the default form submission behavior
const employeeForm = document.getElementById('employeeForm');
employeeForm.addEventListener('submit', (event) => {
  event.preventDefault();
  // Add your code to handle form submission (e.g., add employee to the list)
});

// Get all edit buttons
const editButtons = document.querySelectorAll('.btn-edit');

// Add click event listener to each edit button
editButtons.forEach(button => {
  button.addEventListener('click', () => {
    const employeeId = button.dataset.employeeId;
    // Perform action when edit button is clicked, for example:
    // Redirect to edit page with employeeId as parameter
    window.location.href = `/editEmployee.html?employeeId=${employeeId}`;
  });
});

// Get all delete buttons
const deleteButtons = document.querySelectorAll('.btn-delete');

// Add click event listener to each delete button
deleteButtons.forEach(button => {
  button.addEventListener('click', () => {
    const employeeId = button.dataset.employeeId;
    // Perform action when delete button is clicked, for example:
    // Send request to delete employee with employeeId
    if (confirm('Are you sure you want to delete this employee?')) {
      // Send delete request or perform Firebase delete operation
      // Example:
      // firebase.firestore().collection('employees').doc(employeeId).delete().then(() => {
      //    // Employee deleted successfully
      // }).catch(error => {
      //    console.error('Error deleting employee:', error);
      // });
    }
  });
});

//for perforrmance
// Sample employee data with performance details
const employees = [
  { id: 1, name: "John Doe", goals: "", progress: "", feedback: "" },
  { id: 2, name: "Jane Smith", goals: "", progress: "", feedback: "" },
  // Add more employees as needed
];

// Function to generate input fields for each employee
function generateEmployeeInputs() {
  const employeeList = document.getElementById("employeeList");

  employees.forEach(employee => {
    const employeeDiv = document.createElement("div");
    employeeDiv.classList.add("employee");

    const nameLabel = document.createElement("label");
    nameLabel.textContent = `Employee Name: ${employee.name}`;
    employeeDiv.appendChild(nameLabel);

    const goalsInput = document.createElement("textarea");
    goalsInput.placeholder = "Enter goals for the employee";
    goalsInput.value = employee.goals;
    goalsInput.addEventListener("input", function() {
      employee.goals = this.value;
    });
    employeeDiv.appendChild(goalsInput);

    const progressInput = document.createElement("textarea");
    progressInput.placeholder = "Enter progress for the employee";
    progressInput.value = employee.progress;
    progressInput.addEventListener("input", function() {
      employee.progress = this.value;
    });
    employeeDiv.appendChild(progressInput);

    const feedbackInput = document.createElement("textarea");
    feedbackInput.placeholder = "Provide feedback for the employee";
    feedbackInput.value = employee.feedback;
    feedbackInput.addEventListener("input", function() {
      employee.feedback = this.value;
    });
    employeeDiv.appendChild(feedbackInput);

    employeeList.appendChild(employeeDiv);
  });
}

// Call the function to generate employee inputs
generateEmployeeInputs();

