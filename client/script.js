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
