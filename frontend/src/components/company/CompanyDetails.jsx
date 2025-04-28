import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getCompanyById, updateCompany } from "../../services/company-service";
import { getCompanyUsers, getUsers } from "../../services/user-service";

export default function CompanyDetails() {
  const { id } = useParams();
  const [company, setCompany] = useState(null);
  const [users, setUsers] = useState([]);
  const [editMode, setEditMode] = useState(false);
  const [formData, setFormData] = useState({});
  const navigate = useNavigate();

  useEffect(() => {
    async function fetchData() {
      const companyRes = await getCompanyById(id);
      const usersRes = await getCompanyUsers(id);
      setCompany(companyRes);
      setFormData({
        name: companyRes.name,
        companyEmail: companyRes.companyEmail,
        managerEmail: companyRes.managerEmail,
        registrationCode: companyRes.registrationCode,
      });
      setUsers(usersRes.content);
    }
    fetchData();
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSave = async () => {
    await updateCompany(id, formData).then((response) => {
      if (response.status !== 403) {
        setCompany(response);
      }
    });

    setEditMode(false);
  };

  if (!company) return <div className="p-6">Loading...</div>;

  return (
    <div className="p-6">
      <button
        className="btn btn-sm mb-4"
        onClick={() => navigate("/admin-panel")}
      >
        ← Back to Admin Panel
      </button>

      <div className="mb-6">
        <h2 className="text-2xl font-bold mb-2">Company Details</h2>
        {!editMode ? (
          <>
            <p>
              <strong>Name:</strong> {company.name}
            </p>
            <p>
              <strong>Email:</strong> {company.companyEmail}
            </p>
            <p>
              <strong>Manager Email:</strong> {company.managerEmail}
            </p>
            <p>
              <strong>Registration Code:</strong> {company.registrationCode}
            </p>
            <p>
              <strong>Status:</strong> {company.enabled ? "Active" : "Inactive"}
            </p>
            <button
              className="btn btn-sm mt-4"
              onClick={() => setEditMode(true)}
            >
              Edit
            </button>
          </>
        ) : (
          <>
            <div className="form-control">
              <label className="label">Name</label>
              <input
                className="input input-bordered"
                name="name"
                value={formData.name}
                onChange={handleChange}
              />
            </div>
            <div className="form-control">
              <label className="label">Company Email</label>
              <input
                className="input input-bordered"
                name="companyEmail"
                value={formData.companyEmail}
                onChange={handleChange}
              />
            </div>
            <div className="form-control">
              <label className="label">Manager Email</label>
              <input
                className="input input-bordered"
                name="managerEmail"
                value={formData.managerEmail}
                onChange={handleChange}
              />
            </div>
            <div className="form-control">
              <label className="label">Registration Code</label>
              <input
                className="input input-bordered"
                name="registrationCode"
                value={formData.registrationCode}
                onChange={handleChange}
              />
            </div>
            <div className="mt-4 space-x-2">
              <button className="btn btn-sm btn-primary" onClick={handleSave}>
                Save
              </button>
              <button className="btn btn-sm" onClick={() => setEditMode(false)}>
                Cancel
              </button>
            </div>
          </>
        )}
      </div>

      <div>
        <h3 className="text-xl font-semibold mb-2">Employees</h3>
        <table className="table table-zebra w-full">
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Email</th>
              <th>Role</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.id}>
                <td>{user.id}</td>
                <td>{user.name}</td>
                <td>{user.email}</td>
                <td>{user.role}</td>
                <td>{user.isActive ? "Active" : "Inactive"}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
