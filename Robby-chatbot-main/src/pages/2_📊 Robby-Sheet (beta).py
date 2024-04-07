import streamlit as st
import firebase_admin
from firebase_admin import db
from datetime import datetime

# Check if Firebase app has already been initialized
if not firebase_admin._apps:
    # Initialize Firebase app
    firebase_admin.initialize_app()

# Reference to the root node of the Firebase Realtime Database
ref = db.reference("/")

# Function to save sick leave application to the database
def save_leave_application(email, leave_application_data):
    ref.child("LeaveApplication").push(leave_application_data)

# Function to retrieve attendance data from the database
def get_attendance(emp_id):
    attendance_ref = ref.child("Attendance").child(emp_id)
    attendance_data = attendance_ref.get()
    return attendance_data

# Function to format attendance data into a table
def format_attendance_data(attendance_data, emp_id):
    formatted_data = []
    if attendance_data:
        for date, details in attendance_data.items():
            formatted_row = {
                "Employee ID": emp_id,
                "Date": date,
                "Clock In Time": details.get("checkedin", ""),
                "Clock Out Time": details.get("checkout", ""),
                "Working Hour": details.get("workingHour", ""),
                "Overtime": details.get("overtime", ""),
                "Comp Off": "Yes" if details.get("compOff", False) else "No"
            }
            formatted_data.append(formatted_row)
    return formatted_data

# Function to generate performance analysis text
def generate_performance_text(emp_id):
    # Placeholder function to generate performance analysis text
    performance_text = f"Performance analysis for Employee ID {emp_id}: Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ac libero auctor, ultricies nisl vitae, dignissim eros. Fusce tempor neque non odio posuere, ac tempor mi efficitur. Nulla facilisi. Morbi vestibulum urna quis orci dictum, in vestibulum nulla vehicula. Donec porttitor libero vitae eros blandit, id hendrerit quam vestibulum. Donec"
    return performance_text

# Chatbot interaction
st.title("Robby Chat-Bot 🤖")

# Initial interaction with the chatbot
user_input = st.text_input("How can I help you today?")

# Process user input
if user_input:
    # Check for tags in user input
    if "leave" in user_input.lower():
        st.write("Robby: Sure, please provide the start and end dates for the sick leave.")
        start_date = st.date_input("Start Date:")
        end_date = st.date_input("End Date:")
        if start_date and end_date:
            # Calculate duration of sick leave
            duration = (end_date - start_date).days + 1  # Add 1 to include the end date
            
            # Ask for employee ID, email, type of leave, and reason
            email = st.text_input("Email:")
            leave_type = st.selectbox("Type of Leave", ["Sick Leave", "Vacation", "Personal Leave"])
            reason = st.text_area("Reason:")
            status = "Pending"  # Default status
            
            # Submit sick leave application directly to Firebase Realtime Database without specifying the email as the key
            if st.button("Submit"):
                # Save sick leave application to the database
                leave_application_data = {
                    "email": email,
                    "leaveType": leave_type,
                    "reason": reason,
                    "startDate": start_date.strftime("%d/%m/%Y"),
                    "endDate": end_date.strftime("%d/%m/%Y"),
                    "status": status
                }
                save_leave_application(email, leave_application_data)
                st.success("Sick leave application submitted successfully!")
                
    elif "attendance" in user_input.lower():
        st.write("Robby: Sure, please provide your Employee ID to check your attendance.")
        emp_id = st.text_input("Employee ID:")
        if emp_id:
            # Retrieve attendance data from the database based on the provided employee ID
            attendance_data = get_attendance(emp_id)
            if attendance_data:
                # Format attendance data into a table
                formatted_attendance_data = format_attendance_data(attendance_data, emp_id)
                st.write("Attendance Data:")
                st.dataframe(formatted_attendance_data)  # Display attendance data in table format
            else:
                st.write("Robby: Sorry, no attendance data found for the provided Employee ID.")
    elif "performance" in user_input.lower():
        st.write("Robby: Sure, please provide the Employee ID for performance analysis.")
        emp_id = st.text_input("Employee ID:")
        if emp_id:
            # Here you would fetch relevant data for performance analysis based on the provided employee ID
            # You can then use OpenAI's text generation model to generate a text summarizing the performance
            # For demonstration purposes, let's assume we have some sample performance data
            performance_text = generate_performance_text(emp_id)
            
            # Display the generated performance analysis
            st.write("Performance Analysis:")
            st.write(performance_text)
            
    else:
        st.write("Robby: I'm sorry, I'm not sure how to help with that. Is there something else you need assistance with?")
