import React, { useState, useEffect } from "react";

export default function PartnershipTab({
  partnerRequests,
  activePartners,
  pageSize,
  pages,
  setPages,
  handleRespond,
  handleDeletePartnership,
  formatDate,
  onSendRequest,
  initiatedPartnerships,
  refreshAll,
}) {
  const [targetCompanyId, setTargetCompanyId] = useState("");
  const [searchTerm, setSearchTerm] = useState("");
  const [showDropdown, setShowDropdown] = useState(false);
  const [allCompanies, setAllCompanies] = useState([]);

  const handleSend = () => {
    if (!targetCompanyId) return;
    onSendRequest(localStorage.getItem('companyId'), targetCompanyId).then(() => {
      setTargetCompanyId("");
      setSearchTerm("");
      refreshAll()
    });
  };

  useEffect(() => {
    import("../../../services/company-service").then(({ getActiveCompaniesMin }) => {
      getActiveCompaniesMin()
        .then((data) => setAllCompanies(data || []))
        .catch((err) => console.error("Failed to fetch companies:", err));
    });
  }, []);

  const filteredCompanies = allCompanies.filter((company) =>
    company.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="space-y-12">
      {/* Send Request Form */}
      <div className="mb-6">
        <h3 className="text-lg font-semibold mb-2">Send Partnership Request</h3>
        <div className="flex flex-col sm:flex-row gap-2">
          <div className="relative w-full sm:max-w-xs">
            <input
              type="text"
              placeholder="Search company by name"
              className="input input-bordered w-full rounded-none"
              value={searchTerm}
              onChange={(e) => {
                setSearchTerm(e.target.value);
                setShowDropdown(true);
              }}
              onBlur={() => setTimeout(() => setShowDropdown(false), 150)}
              onFocus={() => setShowDropdown(true)}
            />
            {showDropdown && filteredCompanies.length > 0 && (
              <ul className="menu bg-base-100 w-full mt-1 shadow absolute z-10 max-h-60 overflow-y-auto overflow-x-hidden flex flex-col space-y-1">
                {filteredCompanies.length > 0 ? (
                  filteredCompanies.map((company) => (
                    <li key={company.id} className="w-full">
                      <button type="button" className="text-left w-full px-4 py-2 hover:bg-base-200 truncate"
                        onClick={() => {
                          setTargetCompanyId(company.id);
                          setSearchTerm(company.name);
                          setShowDropdown(false);
                        }}
                      >
                        {company.name}
                      </button>
                    </li>
                  ))
                ) : (
                  <li className="text-sm px-2 py-1 text-gray-500">No matches</li>
                )}
              </ul>
            )}
          </div>
          <button className="btn btn-primary shadow-sm uppercase tracking-wide" onClick={handleSend}>
            Send
          </button>
        </div>
      </div>

      {/* Partnership Requests */}
      <div>
        <h3 className="text-xl font-semibold mb-2">Partnership Requests</h3>
        <ul className="space-y-2">
          {partnerRequests.length > 0 ? (
            partnerRequests.map((req) => (
              <li key={req.id} className="p-4 bg-base-200 border border-base-300">
                <p><strong>From:</strong> {req.initiator.name}</p>
                <p><strong>Status:</strong> {req.status}</p>
                <p><strong>Created at:</strong> {formatDate(req.createdAt)}</p>
                {req.status === "PENDING" && (
                  <div className="mt-2 space-x-2">
                    <button className="btn btn-sm bg-green-500 hover:bg-green-600 text-white border-none" onClick={() => handleRespond(req.id, true)}>Accept</button>
                    <button className="btn btn-sm bg-red-500 hover:bg-red-600 text-white border-none" onClick={() => handleRespond(req.id, false)}>Reject</button>
                  </div>
                )}
              </li>
            ))
          ) : (
            <p className="text-sm text-gray-500">No partnership requests found.</p>
          )}
        </ul>
        {partnerRequests.length >= pageSize && (
          <div className="flex justify-end mt-2 gap-2">
            <button className="btn btn-xs bg-base-300 hover:bg-base-400 text-sm font-medium" onClick={() => setPages(p => ({ ...p, requests: p.requests - 1 }))} disabled={pages.requests === 0}>Prev</button>
            <button className="btn btn-xs bg-base-300 hover:bg-base-400 text-sm font-medium" onClick={() => setPages(p => ({ ...p, requests: p.requests + 1 }))}>Next</button>
          </div>
        )}
      </div>

      {/* Sent Pending Requests */}
      <div>
        <h3 className="text-xl font-semibold mb-2">Sent Requests (Pending)</h3>
        <ul className="space-y-2">
          {initiatedPartnerships.length > 0 ? (
            initiatedPartnerships.map((req) => (
              <li key={req.id} className="p-4 bg-base-100 border border-base-300">
                <p><strong>To:</strong> {req.partner.name}</p>
                <p><strong>Status:</strong> {req.status}</p>
                <p><strong>Created at:</strong> {formatDate(req.createdAt)}</p>
                <button className="btn btn-xs bg-red-500 hover:bg-red-600 text-white border-none mt-2" onClick={() => handleDeletePartnership(req.id)}>Remove</button>
              </li>
            ))
          ) : (
            <p className="text-sm text-gray-500">No pending sent requests.</p>
          )}
        </ul>
      </div>

      {/* Active Partnerships */}
      <div>
        <h3 className="text-xl font-semibold mb-2">Active Partnerships</h3>
        <ul className="space-y-2">
          {activePartners.length > 0 ? (
            activePartners.map((p) => (
              <li key={p.id} className="p-4 bg-base-100 border border-base-300">
                <p><strong>Initiator:</strong> {p.initiator.name}</p>
                <p><strong>Partner:</strong> {p.partner.name}</p>
                <p><strong>Created at:</strong> {formatDate(p.createdAt)}</p>
                <button className="btn btn-xs bg-red-500 hover:bg-red-600 text-white border-none mt-2" onClick={() => handleDeletePartnership(p.id)}>Remove</button>
              </li>
            ))
          ) : (
            <p className="text-sm text-gray-500">No active partnerships.</p>
          )}
        </ul>
        {activePartners.length >= pageSize && (
          <div className="flex justify-end mt-2 gap-2">
            <button className="btn btn-xs" onClick={() => setPages(p => ({ ...p, partners: p.partners - 1 }))} disabled={pages.partners === 0}>Prev</button>
            <button className="btn btn-xs" onClick={() => setPages(p => ({ ...p, partners: p.partners + 1 }))}>Next</button>
          </div>
        )}
      </div>
    </div>
  );
}

