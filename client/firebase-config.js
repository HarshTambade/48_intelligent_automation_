// firebase-config.js
import { initializeApp } from "https://www.gstatic.com/firebasejs/10.10.0/firebase-app.js";

const firebaseConfig = {
    apiKey: "AIzaSyD6A4gvA4xt2tfvK2S1UOsg25OYFsnoHBk",
    authDomain: "employee-management-111.firebaseapp.com",
    databaseURL: "https://employee-management-111-default-rtdb.firebaseio.com",
    projectId: "employee-management-111",
    storageBucket: "employee-management-111.appspot.com",
    messagingSenderId: "511443056601",
    appId: "1:511443056601:web:02ba1eafbdb2e5f3dc4bc8",
    measurementId: "G-PG3FT3F8WL"
  };

const firebaseApp = initializeApp(firebaseConfig);

export default firebaseApp;
