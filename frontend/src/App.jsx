import { Outlet, redirect } from "react-router-dom";
import Navbar from "./components/partials/navbar";
import { useEffect } from "react";
import { isAuth } from "./services/auth-service";

export default function App() {
  useEffect(() => {
    console.log(isAuth());
    if (isAuth() == null) {
      redirect("/login");
    }
  });

  return (
    <>
      <Navbar />
      <div className="container mx-auto">
        <Outlet />
      </div>
    </>
  );
}
