import { useEffect, useState } from "react";
import InactiveAccountNotice from "../partials/InactiveAccountNotice";
import InactiveCompanyNotice from "../partials/InactiveCompanyNotice";
import { getUserDataMin } from "../../services/user-service";
import CreateCompanyPrompt from "../partials/CreateCompanyPrompt";
import CompanyDashboard from "../company/dashboard/CompanyDashboard";
import UserHome from "../user/UserHome";

export default () => {
  const [user, setUser] = useState(false);

  const fetchUserData = async () => {
    const userMinData = await getUserDataMin(localStorage.getItem("userId"));
    localStorage.setItem("companyId", userMinData.companyId);
    setUser(userMinData);
  };

  useEffect(() => {
    fetchUserData();
  }, []);

  if (!user) return null;


  if (!user.companyName) {
    return <CreateCompanyPrompt user={user} />;
  }

  if (!user.isActive && user.role === 'DISABLED_USER') {
    return <InactiveAccountNotice />;
  } else if (!user.isActive && user.role === 'COMPANY_OWNER') {
    return <InactiveCompanyNotice />;
  } else if (user.role === 'ADMIN') {
    return redirect('/admin-panel')
  } else if (user.isActive && user.role === 'COMPANY_OWNER') {
    return <CompanyDashboard />;
  } else if (user.isActive && user.role === 'EMPLOYEE') {
    return <UserHome />
  } else {
    console.log(user)
  }

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold">Welcome, {user.name}!</h1>
      <p className="mt-2 text-base text-gray-600">
        This is your company dashboard. Manage your promotions and partnerships here.
      </p>
    </div>
  );
};
