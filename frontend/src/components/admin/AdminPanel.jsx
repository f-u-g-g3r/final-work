import React, { useState, useEffect } from "react";
import CompanyManagement from "./CompanyManagement";
import UserManagement from "./UserManagement";

export default function AdminPanel() {
  const [selectedTab, setSelectedTab] = useState("Companies");

  const renderContent = () => {
    switch (selectedTab) {
      case "Companies":
        return <CompanyManagement />;
      case "Users":
        return <UserManagement />;
      default:
        return null;
    }
  };

  return (
    <div className="w-screen min-h-screen flex relative left-1/2 right-1/2 -mx-[50vw]">
      {/* Sidebar */}
      <div className="w-64 bg-base-200 p-4 border-r border-base-300 text-center">
        <h2 className="text-xl font-bold mb-4">Admin Panel</h2>
        <ul className="flex flex-col gap-2 w-full">
          <li>
            <button
              className={`btn btn-ghost w-full justify-start ${
                selectedTab === "Companies" ? "bg-gray-300 font-semibold" : ""
              }`}
              onClick={() => setSelectedTab("Companies")}
            >
              Companies
            </button>
          </li>
          <li>
            <button
              className={`btn btn-ghost w-full justify-start ${
                selectedTab === "Users" ? "bg-gray-300 font-semibold" : ""
              }`}
              onClick={() => setSelectedTab("Users")}
            >
              Users
            </button>
          </li>
        </ul>
      </div>

      {/* Content */}
      <div className="flex-1 bg-base-100">{renderContent()}</div>
    </div>
  );
}
