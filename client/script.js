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
