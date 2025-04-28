import React from "react";
import { Link } from "react-router-dom";
import CompanyRegistration from "../company-registration";

export default function CreateCompanyPrompt() {
  return (
    <div className="flex justify-center mt-24 px-4">
      <div className="card bg-base-100 shadow-xl p-8 max-w-md text-center w-full">
        <h2 className="text-2xl font-bold mb-4">No Company Assigned</h2>
        <p className="text-base mb-6">
          You are not currently linked to any company. You can create your own company to get started.
        </p>
        
        <CompanyRegistration />
      </div>
    </div>
  );
}
