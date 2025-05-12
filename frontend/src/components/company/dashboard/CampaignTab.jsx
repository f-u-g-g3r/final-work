import React, { useEffect, useState } from "react";
import {
  getInitiatedCampaigns,
  createCampaign,
  updateCampaign,
  deleteCampaign,
} from "../../../services/campaign-service";

export default function CampaignTab({
  activePartners,
  pageSize,
  pages,
  setPages,
  formatDate,
  refreshAll,
}) {
  const [campaigns, setCampaigns] = useState([]);
  const [newCampaign, setNewCampaign] = useState({
    title: "",
    description: "",
    discountPercentage: 0,
    validUntil: "",
    partnershipsIds: [],
  });
  const [selectedPartnerships, setSelectedPartnerships] = useState([]);
  const [partnerSearch, setPartnerSearch] = useState("");
  const [showPartnerDropdown, setShowPartnerDropdown] = useState(false);
  const [editingCampaign, setEditingCampaign] = useState(null);

  useEffect(() => {
    getInitiatedCampaigns(localStorage.getItem("companyId"), {
      pagingOptions: {
        page: pages.campaigns,
        pageSize: pageSize,
      },
      sortOptions: {
        sortBy: "id",
        direction: 0,
      },
    }).then((res) => {
      setCampaigns(res.content || []);
    });
  }, [pages.campaigns]);

  const handleCreate = () => {
    if (
      !newCampaign.title ||
      !newCampaign.discountPercentage ||
      newCampaign.partnershipsIds.length == 0
    )
      return;
    if (newCampaign.validUntil) {
      const dateObj = new Date(newCampaign.validUntil);
      if (isNaN(dateObj.getTime())) {
        alert("Please enter a valid date");
        return;
      }
      newCampaign.validUntil = dateObj.toISOString().split(".")[0] + ".000000";
      console.log(newCampaign.validUntil);
    }

    createCampaign({
      ...newCampaign,
      initiatorId: localStorage.getItem("companyId"),
      partnershipsIds: selectedPartnerships.map((p) => p.id),
    }).then(() => {
      setNewCampaign({
        title: "",
        description: "",
        discountPercentage: 0,
        validUntil: "",
        partnershipsIds: [],
      });
      getInitiatedCampaigns(localStorage.getItem("companyId")).then((res) => {
        setCampaigns(res.content || []);
        refreshAll();
      });
    });
  };

  const handleSelectPartnerhip = (partnership) => {
    if (!selectedPartnerships.find((p) => p.id === partnership.id)) {
      setSelectedPartnerships((prev) => [...prev, partnership]);
      setPartnerSearch("");
    }
  };

  const handleRemovePartner = (id) => {
    setSelectedPartnerships((prev) => prev.filter((p) => p.id !== id));
  };

  const handleDelete = (id) => {
    deleteCampaign(id).then(() => {
      setCampaigns((prev) => prev.filter((c) => c.id !== id));
      refreshAll();
    });
  };

  const handleEditClick = (campaign) => {
    setEditingCampaign({
      id: campaign.id,
      title: campaign.title,
      description: campaign.description,
      discountPercentage: campaign.discountPercentage,
      validUntil: campaign.validUntil?.split("T")[0] || "",
    });

    document.getElementById("editCampaignModal").showModal();
  };

  const handleUpdate = (e) => {
    e.preventDefault();
    const updated = {
      ...editingCampaign,
      validUntil: editingCampaign.validUntil
        ? new Date(editingCampaign.validUntil).toISOString().split(".")[0] +
          ".000000"
        : null,
    };
    updateCampaign(updated.id, updated).then(() => {
    document.getElementById("editCampaignModal").close();
      setEditingCampaign(null);
      getInitiatedCampaigns(localStorage.getItem("companyId")).then((res) => {
        setCampaigns(res.content || []);
        refreshAll();
      });
    });
  };

  return (
    <div className="space-y-12">
      <div className="mb-6">
        <button
          className="btn btn btn-primary"
          onClick={() =>
            document.getElementById("createCampaignModal").showModal()
          }
        >
          Create campaign
        </button>

        <dialog id="createCampaignModal" className="modal">
          <div className="modal-box w-11/12 max-w-5xl">
            <h3 className="font-bold text-lg">Create new campaign</h3>

            <div className="mt-4 space-y-4 p-4 bg-base-100">
              <input
                type="text"
                className="input input-bordered w-full"
                placeholder="Title"
                value={newCampaign.title}
                onChange={(e) =>
                  setNewCampaign({ ...newCampaign, title: e.target.value })
                }
              />
              <textarea
                className="input textarea textarea-bordered w-full"
                placeholder="Description"
                value={newCampaign.description}
                onChange={(e) =>
                  setNewCampaign({
                    ...newCampaign,
                    description: e.target.value,
                  })
                }
              />
              <label className="input">
                <input
                  type="number"
                  className="grow"
                  placeholder="Discount %"
                  value={newCampaign.discountPercentage}
                  onChange={(e) =>
                    setNewCampaign({
                      ...newCampaign,
                      discountPercentage: parseFloat(e.target.value),
                    })
                  }
                />
                %
              </label>
              <input
                type="date"
                className="input input-bordered w-full"
                value={newCampaign.validUntil}
                onChange={(e) =>
                  setNewCampaign({
                    ...newCampaign,
                    validUntil: e.target.value,
                  })
                }
              />
              <div className="space-y-2">
                <label className="block font-semibold">Beneficiaries</label>

                {/* Selected */}
                <div className="flex flex-wrap gap-2">
                  {selectedPartnerships.map((p) => (
                    <div
                      key={
                        p.initiator.id.toString() ===
                        localStorage.getItem("companyId")
                          ? p.partner.id
                          : p.initiator.id
                      }
                      className="badge badge-neutral gap-1"
                    >
                      {p.initiator.id.toString() ===
                      localStorage.getItem("companyId")
                        ? p.partner.name
                        : p.initiator.name}
                      <button
                        type="button"
                        className="ml-1 font-bold"
                        onClick={() => handleRemovePartner(p.id)}
                      >
                        ✕
                      </button>
                    </div>
                  ))}
                </div>

                {/* Search Input */}
                <div className="relative">
                  <input
                    type="text"
                    placeholder="Search partners..."
                    className="input input-bordered w-full"
                    value={partnerSearch}
                    onChange={(e) => {
                      setPartnerSearch(e.target.value);
                      setShowPartnerDropdown(true);
                    }}
                    onFocus={() => setShowPartnerDropdown(true)}
                    onBlur={() =>
                      setTimeout(() => setShowPartnerDropdown(false), 150)
                    }
                  />
                  {showPartnerDropdown && (
                    <ul className="absolute w-full z-10 bg-base-100 border border-base-300 shadow mt-1 max-h-60 overflow-auto">
                      {activePartners
                        .filter((p) => {
                          if (
                            p.initiator.id.toString() ===
                            localStorage.getItem("companyId")
                          ) {
                            return (
                              p.partner.name
                                .toLowerCase()
                                .includes(partnerSearch.toLowerCase()) &&
                              !selectedPartnerships.some((sp) => sp.id === p.id)
                            );
                          } else {
                            return (
                              p.initiator.name
                                .toLowerCase()
                                .includes(partnerSearch.toLowerCase()) &&
                              !selectedPartnerships.some((sp) => sp.id === p.id)
                            );
                          }
                        })
                        .map((p) => (
                          <li key={p.id}>
                            <button
                              type="button"
                              className="w-full text-left px-3 py-2 hover:bg-base-200"
                              onClick={() => handleSelectPartnerhip(p)}
                            >
                              {p.initiator.id.toString() ===
                              localStorage.getItem("companyId")
                                ? p.partner.name
                                : p.initiator.name}
                            </button>
                          </li>
                        ))}
                      {activePartners.filter((p) => {
                        if (
                          p.initiator.id.toString() ===
                          localStorage.getItem("companyId")
                        ) {
                          return (
                            p.partner.name
                              .toLowerCase()
                              .includes(partnerSearch.toLowerCase()) &&
                            !selectedPartnerships.some((sp) => sp.id === p.id)
                          );
                        } else {
                          return (
                            p.initiator.name
                              .toLowerCase()
                              .includes(partnerSearch.toLowerCase()) &&
                            !selectedPartnerships.some((sp) => sp.id === p.id)
                          );
                        }
                      }).length === 0 && (
                        <li className="text-sm text-gray-500 px-3 py-2">
                          No matches
                        </li>
                      )}
                    </ul>
                  )}
                </div>
              </div>

              <div className="modal-action">
                <form method="dialog">
                  {/* if there is a button, it will close the modal */}
                  <button className="btn mx-5">Cancel</button>
                  <button className="btn btn-success" onClick={handleCreate}>
                    Submit Campaign
                  </button>
                </form>
              </div>
            </div>
          </div>
        </dialog>

        <dialog id="editCampaignModal" className="modal">
          <div className="modal-box w-11/12 max-w-3xl">
            <h3 className="font-bold text-lg">Edit Campaign</h3>

            {editingCampaign && (
              <div className="mt-4 space-y-4 p-4 bg-base-100">
                <input
                  type="text"
                  className="input input-bordered w-full"
                  placeholder="Title"
                  value={editingCampaign.title}
                  onChange={(e) =>
                    setEditingCampaign({
                      ...editingCampaign,
                      title: e.target.value,
                    })
                  }/>
                <textarea
                  className="input textarea textarea-bordered w-full"
                  placeholder="Description"
                  value={editingCampaign.description}
                  onChange={(e) =>
                    setEditingCampaign({
                      ...editingCampaign,
                      description: e.target.value,
                    })
                  }
                />
                <label className="input">
                  <input
                    type="number"
                    className="grow"
                    placeholder="Discount %"
                    value={editingCampaign.discountPercentage}
                    onChange={(e) =>
                      setEditingCampaign({
                        ...editingCampaign,
                        discountPercentage: parseFloat(e.target.value),
                      })
                    }
                  />
                  %
                </label>
                <input
                  type="date"
                  className="input input-bordered w-full"
                  value={editingCampaign.validUntil}
                  onChange={(e) =>
                    setEditingCampaign({
                      ...editingCampaign,
                      validUntil: e.target.value,
                    })
                  }
                />

                <div className="modal-action">
                  <form method="dialog">
                    <button className="btn mx-5">Cancel</button>
                    <button
                      className="btn btn-success"
                      onClick={(e) => handleUpdate(e)}
                    >
                      Save Changes
                    </button>
                  </form>
                </div>
              </div>
            )}
          </div>
        </dialog>
      </div>
      <div>
        <h3 className="text-xl font-semibold mb-2">Campaigns</h3>
        <ul className="space-y-2">
          {campaigns.length > 0 ? (
            campaigns.map((c) => (
              <li key={c.id} className="p-4 bg-base-100 border border-base-300">
                <p>
                  <strong>{c.title}</strong>
                </p>
                <p>{c.description}</p>
                <p>
                  <strong>Discount:</strong> {c.discountPercentage}%
                </p>
                <p>
                  <strong>Valid Until:</strong>{" "}
                  {formatDate(c.validUntil, "DD.MM.YYYY") || "Not set"}
                </p>
                <p>
                  <strong>Beneficiaries:</strong>{" "}
                  {c.partnerships.map((p, index, arr) => (
                    <span key={p.id}>
                      {p.initiator.id.toString() ===
                      localStorage.getItem("companyId")
                        ? p.partner.name
                        : p.initiator.name}
                      {index < arr.length - 1 ? ", " : ""}
                    </span>
                  ))}
                </p>
                <div className="mt-2 space-x-2">
                  <button
                    className="btn btn-sm"
                    onClick={() => handleEditClick(c)}
                  >
                    Edit
                  </button>
                  <button
                    className="btn btn-sm btn-error"
                    onClick={() => handleDelete(c.id)}
                  >
                    Delete
                  </button>
                </div>
              </li>
            ))
          ) : (
            <p className="text-sm text-gray-500">No campaigns found.</p>
          )}
        </ul>

        <div className="flex justify-end mt-2 gap-2 mb-20">
          <button
            className="btn"
            onClick={() =>
              setPages((p) => ({ ...p, campaigns: p.campaigns - 1 }))
            }
            disabled={pages.campaigns === 0}
          >
            Prev
          </button>
          <button
            className="btn"
            onClick={() =>
              setPages((p) => ({ ...p, campaigns: p.campaigns + 1 }))
            }
            disabled={campaigns.length < pageSize}
          >
            Next
          </button>
        </div>
      </div>
    </div>
  );
}
