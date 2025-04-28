import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import App from "./App.jsx";
import { createBrowserRouter, Route, RouterProvider } from "react-router-dom";
import ErrorPage from "./components/error-page.jsx";
import Login, { action, loginLoader } from "./components/auth/login.jsx";
import SignUp, { signUpAction } from "./components/auth/sign-up.jsx";
import CompanyRegistration, {
  registerCompanyAction,
} from "./components/company-registration.jsx";
import Home from "./components/home/home.jsx";
import AdminPanel from "./components/admin/AdminPanel.jsx";
import CompanyDetails from "./components/company/CompanyDetails.jsx";
import ScanQrPage from "./components/user/ScanQrPage.jsx";

const router = createBrowserRouter([
  {
    path: "/",
    element: <App />,
    errorElement: <ErrorPage />,
    children: [
      {
        path: "/home",
        element: <Home />,
        errorElement: <ErrorPage />,
        action: registerCompanyAction,
      },
      {
        path: "/admin-panel",
        element: <AdminPanel />,
        errorElement: <ErrorPage />,
      },
      {
        path: "/admin/companies/:id",
        element: <CompanyDetails />,
        errorElement: <ErrorPage />,
      },
    ],
  },
  {
    path: "/login",
    loader: loginLoader,
    action: action,
    element: <Login />,
    errorElement: <ErrorPage />,
  },
  {
    path: "/sign-up",
    loader: loginLoader,
    action: signUpAction,
    element: <SignUp />,
    errorElement: <ErrorPage />,
  },
  {
    path: "/register-company",
    element: <CompanyRegistration />,
    errorElement: <ErrorPage />,
  },
  {
    path: "/qr/scan/:uuid",
    element: <ScanQrPage />,
    errorElement: <ErrorPage />,
  },
]);

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <RouterProvider router={router} />
  </StrictMode>
);
