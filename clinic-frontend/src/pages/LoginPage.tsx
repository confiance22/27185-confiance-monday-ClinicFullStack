import React, { useState } from "react";
import Button from "../components/Button";

function LoginPage() {
    const [isLogin, setIsLogin] = useState(true);
    //LOGIN
    const [userName, setUserName] = useState("");
    const [userPassword, setUserPassword] = useState("");

    // CREATE ACCOUNT
    const [studentId, setStudentId] = useState("");
    const [email, setEmail] = useState("");
    const [createPassword, setCreatePassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    function CheckLogin() {
        alert("this username for this user is " + userName + " and password is " + userPassword);
    }

    function HandleCreateAccount() {
        alert("Creating account for Student ID: " + studentId + " with Email: " + email);
    }
    const boxStyle = {border: "3px solid black",margin: "200px auto 20px auto",padding: "30px", borderRadius: "0px", width: "350px",};
    return (
        <div>
            {isLogin ? (
                <div style={boxStyle}>
                    <h1>Sign In</h1>
                    <h3>Access your clinic portal</h3>

                    <label>UserName:</label>
                    <input style={{ border: "1px solid #9ca3af", padding: "4px 8px", borderRadius: "0px" }} type="text" placeholder="Please enter your user name" value={userName} onChange={(e) => setUserName(e.target.value)}/><br></br>

                    <label>Password:</label>
                    <input style={{ border: "1px solid #9ca3af", padding: "4px 8px", borderRadius: "0px" }} type="password" placeholder="Enter your password" value={userPassword}onChange={(e) => setUserPassword(e.target.value)}/>

                    <div>
                        <Button buttonlabel="LOGIN" action={CheckLogin} />
                        <label>New here? </label>
                        <Button buttonlabel="Create an Account" action={() => setIsLogin(false)} />
                    </div>
                </div>
            ) : (
                <div style={{ ...boxStyle, border: "5px solid black" }}>
                    <h3>Create an Account</h3>
                    <h3>Access your clinic portal</h3>

                    <label>Student ID:</label>
                    <input style={{ border: "1px solid #9ca3af", padding: "4px 8px", borderRadius: "0px" }} type="text" placeholder="Please enter your student ID" value={studentId} onChange={(e) => setStudentId(e.target.value)}/><br></br>

                    <label>Email:</label>
                    <input style={{ border: "1px solid #9ca3af", padding: "4px 8px", borderRadius: "0px" }} type="text" placeholder="Enter your email" value={email} onChange={(e) => setEmail(e.target.value)}/><br></br>

                    <label>Password:</label>
                    <input style={{ border: "1px solid #9ca3af", padding: "4px 8px", borderRadius: "0px" }} type="password" placeholder="password" value={createPassword} onChange={(e) => setCreatePassword(e.target.value)}/><br></br>

                    <label>Confirm:</label>
                    <input style={{ border: "1px solid #9ca3af", padding: "4px 8px", borderRadius: "0px" }} type="password"  placeholder="Confirm your password" name= "confirmpassword"value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)}/><br></br>
                        {/* <script>{isLogin === false && createPassword !== confirmPassword && (
                        <p style={{ color: "red" }}>Passwords do not match!</p>
                        )}</script> */}
                    <div>
                        <Button buttonlabel="CREATE AN ACCOUNT" action={HandleCreateAccount} />
                        <label>Already have an account? </label>
                        <Button buttonlabel="Sign In" action={() => setIsLogin(true)} />
                    </div>
                </div>
            )}
        </div>
    );
}

export default LoginPage;