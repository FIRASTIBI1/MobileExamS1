// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";
import { getAuth, signInWithEmailAndPassword } from "firebase/auth";
import { getFirestore } from "firebase/firestore";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyDRvEzXv6u83RRUE6gBXWOdhJ2-3CAvFSk",
  authDomain: "kaibiabsence.firebaseapp.com",
  projectId: "kaibiabsence",
  storageBucket: "kaibiabsence.firebasestorage.app",
  messagingSenderId: "844890440440",
  appId: "1:844890440440:web:0b8d3709119deda35a710d",
  measurementId: "G-6PD2CQ9YY6",
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);
const auth = getAuth(app);
export const db = getFirestore(app);
export { auth, signInWithEmailAndPassword };
