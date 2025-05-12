import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getCompanies, activateCompany, deactivateCompany } from "../../services/company-service";

export default function CompanyManagement() {
  const [companies, setCompanies] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [sortBy, setSortBy] = useState("id");
  const [direction, setDirection] = useState(0);
  const [search, setSearch] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const request = {
      pagingOptions: {
        page: page,
        pageSize: 15,
      },
      sortOptions: {
        sortBy,
        direction,
      },
    };

    if (search.trim() !== "") {
      request.filterOptions = [
        {
          filterBy: "generalSearch",
          filterValue: search,
        },
      ];
    }

    getCompanies(request).then((res) => {
      setCompanies(res.content);
      setTotalPages(res.page.totalPages);
    });
  }, [page, sortBy, direction, search]);

  const handleToggleStatus = async (id, isEnabled) => {
    try {
      if (isEnabled) {
        await deactivateCompany(id);
      } else {
        await activateCompany(id);
      }
      setCompanies((prev) =>
        prev.map((company) =>
          company.id === id ? { ...company, isEnabled: !company.isEnabled } : company
        )
      );
    } catch (e) {
      console.error("Failed to toggle company status", e);
    }
  };

  const handleSort = (field) => {
    if (sortBy === field) {
      setDirection((prev) => (prev === 0 ? 1 : 0));
    } else {
      setDirection(0);
      setSortBy(field);
    }
  };

  
  if (companies.length < 2) return <div className="p-6">Loading...</div>;

  return (
    <div className="p-6">
      <h3 className="text-xl font-bold mb-4">Manage Companies</h3>

      <div className="mb-4">
        <input
          type="text"
          placeholder="Search..."
          className="input input-bordered w-full max-w-xs"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
      </div>

      <div className="overflow-x-auto">
        <table className="table w-full">
          <thead>
            <tr>
              <th onClick={() => handleSort("id")} className="cursor-pointer">ID {sortBy === "id" ? (direction === 0 ? "▼" : "▲") : ""}</th>
              <th onClick={() => handleSort("name")} className="cursor-pointer">Name {sortBy === "name" ? (direction === 0 ? "▼" : "▲") : ""}</th>
              <th onClick={() => handleSort("companyEmail")} className="cursor-pointer">Email {sortBy === "companyEmail" ? (direction === 0 ? "▼" : "▲") : ""}</th>
              <th onClick={() => handleSort("managerEmail")} className="cursor-pointer">Manager Email {sortBy === "managerEmail" ? (direction === 0 ? "▼" : "▲") : ""}</th>
              <th onClick={() => handleSort("registrationCode")} className="cursor-pointer">Reg. Code {sortBy === "registrationCode" ? (direction === 0 ? "▼" : "▲") : ""}</th>
              <th onClick={() => handleSort("isEnabled")} className="cursor-pointer">Status {sortBy === "isEnabled" ? (direction === 0 ? "▼" : "▲") : ""}</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {companies.map((company) => (
              <tr key={company.id} className="cursor-pointer hover:bg-base-300" onClick={() => navigate(`/admin/companies/${company.id}`)}>
                <td className="w-[60px] whitespace-nowrap">{company.id}</td>
                <td className="w-[300px] truncate whitespace-nowrap">{company.name}</td>
                <td className="w-[350px] truncate whitespace-nowrap">{company.companyEmail}</td>
                <td className="w-[350px] truncate whitespace-nowrap">{company.managerEmail}</td>
                <td className="w-[300px] truncate whitespace-nowrap">{company.registrationCode}</td>
                <td>
                  <span className={company.isEnabled ? "text-success" : "text-error"}>
                    {company.isEnabled ? "Active" : "Inactive"}
                  </span>
                </td>
                <td className="space-x-2">
                  <button
                    className="btn btn-xs btn-outline"
                    onClick={(e) => {
                      e.stopPropagation();
                      handleToggleStatus(company.id, company.isEnabled);
                    }}
                  >
                    {company.isEnabled ? "Deactivate" : "Activate"}
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="flex justify-center mt-4 gap-2">
        <button className="btn btn-sm" disabled={page === 0} onClick={() => setPage((p) => p - 1)}>Prev</button>
        <span className="text-sm pt-2">Page {page + 1} of {totalPages}</span>
        <button className="btn btn-sm" disabled={page + 1 >= totalPages} onClick={() => setPage((p) => p + 1)}>Next</button>
      </div>
    </div>
  );
}
