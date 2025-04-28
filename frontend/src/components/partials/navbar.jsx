import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getUserDataMin } from "../../services/user-service";

export default function Navbar() {
  const logOut = () => {
    localStorage.clear();
  };

  const [userName, setUserName] = useState("");
  const [companyName, setCompanyName] = useState("");

  const fetchUserData = async () => {
    const userMinData = await getUserDataMin(localStorage.getItem("userId"));
    setUserName(userMinData.name);
    setCompanyName(userMinData.companyName);
  };

  useEffect(() => {
    fetchUserData();
  }, []);

  return (
    <div className="navbar bg-base-100 shadow mb-1">
      <div className="flex-1"></div>
      <div className="navbar-end">
        {/* 👤 User Info */}
        <div className="text-sm text-right mr-4">
          <div className="text-lg font-semibold">{userName}</div>
          <div className="text-base opacity-70">{companyName}</div>
        </div>

        <Link className="btn" to={`/login`} onClick={logOut}>
          Log out
        </Link>
      </div>
    </div>
  );
}
