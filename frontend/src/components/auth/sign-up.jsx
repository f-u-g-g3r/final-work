import React, { useEffect, useState } from "react";
import { Form, Link, redirect } from "react-router-dom";
import { getActiveCompaniesMin } from "../../services/company-service";
import { register } from "../../services/auth-service";

export async function signUpAction({ request }) {
  const formData = await request.formData();
  const userData = Object.fromEntries(formData);
  const parsedCompanyId = parseInt(userData.companyId, 10);
  userData.companyId = isNaN(parsedCompanyId) ? -1 : parsedCompanyId;

  try {
    const { data } = await register(userData);

    localStorage.setItem("token", data.token);
    localStorage.setItem("authority", data.role);
    localStorage.setItem("uuid", data.uuid);
    localStorage.setItem("userId", data.id);

    return redirect("/home");
  } catch (e) {
    return redirect("/sign-up");
  }
}

export function loginLoader() {
  if (isAuth()) {
    return redirect("/home");
  }
  return null;
}

export default function SignUp() {
  const [showPassword, setShowPassword] = useState(false);
  const [companies, setCompanies] = useState([]);
  const [filteredCompanies, setFilteredCompanies] = useState([]);
  const [companyInput, setCompanyInput] = useState("");
  const [selectedCompanyId, setSelectedCompanyId] = useState(null);
  const [showDropdown, setShowDropdown] = useState(false);

  const fetchAndSetCompanies = async () => {
    try {
      const result = await getActiveCompaniesMin();
      setCompanies(result);
      setFilteredCompanies(result);

      // Set default company
      const defaultCompanyId = "-1";
      const defaultCompany = "No company is set";

      if (defaultCompany) {
        setCompanyInput("No company is set");
        setSelectedCompanyId(defaultCompanyId);
      }
    } catch (e) {
      console.error("Error fetching companies:", e);
    }
  };

  useEffect(() => {
    fetchAndSetCompanies();
  }, []);

  const handleCompanyInputChange = (e) => {
    const value = e.target.value;
    setCompanyInput(value);
    setShowDropdown(true);

    const filtered = companies.filter((c) =>
      c.name.toLowerCase().includes(value.toLowerCase())
    );
    setFilteredCompanies(filtered);
  };

  const handleSelectCompany = (company) => {
    setCompanyInput(company.name);
    setSelectedCompanyId(company.id);
    setShowDropdown(false);
  };

  return (
    <div className="flex justify-center items-center min-h-screen bg-base-200">
      <div className="card w-96 bg-base-100 shadow-xl p-6">
        <h2 className="text-2xl font-bold text-center">Sign Up</h2>

        <Form method="post">
          <div className="card-body">
            {/* Full Name */}
            <div className="form-control text-left">
              <label className="label">
                <span className="label-text block text-left">Full name</span>
              </label>
              <input
                type="text"
                placeholder="Enter your full name"
                className="input input-bordered w-full"
                required
                name="name"
              />
            </div>

            {/* Email */}
            <div className="form-control mt-2 text-left">
              <label className="label">
                <span className="label-text block text-left">Email</span>
              </label>
              <input
                type="email"
                placeholder="Enter your email"
                className="input input-bordered w-full"
                required
                name="email"
              />
            </div>

            {/* Password */}
            <div className="form-control mt-2 text-left">
              <label className="label">
                <span className="label-text block text-left">Password</span>
              </label>
              <div className="relative">
                <input
                  type={showPassword ? "text" : "password"}
                  placeholder="Enter your password"
                  className="input input-bordered w-full"
                  required
                  name="password"
                />
                <button
                  type="button"
                  className="absolute right-2 top-2 text-sm text-primary"
                  onClick={() => setShowPassword(!showPassword)}
                >
                  {showPassword ? "Hide" : "Show"}
                </button>
              </div>
            </div>

            {/* Company Autocomplete */}
            <div className="form-control mt-2 text-left relative">
              <label className="label">
                <span className="label-text block text-left">Company</span>
              </label>
              <input
                type="text"
                placeholder="Type to search company"
                className="input input-bordered w-full"
                value={companyInput}
                onChange={handleCompanyInputChange}
                onFocus={() => setShowDropdown(true)}
                onBlur={() => setTimeout(() => setShowDropdown(false), 200)}
              />
              {showDropdown && filteredCompanies.length > 0 && (
                <ul className="menu bg-base-100 w-full mt-1 shadow absolute z-10 max-h-60 overflow-auto">
                  {companyInput.length === 0 ? (
                    <li className="rounded-none" key={-1}>
                      <button
                        type="button"
                        onClick={() =>
                          handleSelectCompany({
                            id: -1,
                            name: "No company is set",
                          })
                        }
                      >
                        No company is set
                      </button>
                    </li>
                  ) : (
                    <></>
                  )}
                  {filteredCompanies.map((company) => (
                    <li key={company.id}>
                      <button
                        className="rounded-none"
                        type="button"
                        onClick={() => handleSelectCompany(company)}
                      >
                        {company.name}
                      </button>
                    </li>
                  ))}
                </ul>
              )}
            </div>

            {/* Hidden input to pass selected company ID */}
            <input
              type="hidden"
              name="companyId"
              value={selectedCompanyId ?? ""}
            />

            <div className="form-control mt-4 text-center">
              <button className="btn btn-primary">Sign Up</button>
            </div>

            <div className="text-center mt-2">
              <Link to={`/login`} className="text-sm text-primary">
                Login
              </Link>
            </div>
          </div>
        </Form>
      </div>
    </div>
  );
}
