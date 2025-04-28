import React, { useEffect, useState } from "react";
import {
  getPartnershipRequests,
  sendPartnershipRequest,
  respondToPartnership,
  getActivePartnerships,
  deletePartnership,
  getInitiatedPartnerships,
} from "../../../services/partnership-service";
import {
  getCompanyUsers,
  activateUser,
  deactivateUser,
} from "../../../services/user-service";
import {
  getInitiatedCampaigns,
  getBeneficiaryOfCompany,
} from "../../../services/campaign-service";
import { formatDate } from "../../../utils/utils";
import PartnershipTab from "./PartnershipTab";
import UserTab from "./UserTab";
import CampaignTab from "./CampaignTab";
import { addUserToCompanyByUuid } from "../../../services/company-service";

export default function CompanyDashboard() {
  const [activeTab, setActiveTab] = useState("partnerships");
  const [partnerRequests, setPartnerRequests] = useState([]);
  const [initiatedPartnerships, setInitiatedPartnerships] = useState([]);
  const [activePartners, setActivePartners] = useState([]);
  const [users, setUsers] = useState([]);
  const [initiatedCampaigns, setInitiatedCampaigns] = useState([]);
  const [beneficiaryOfCompany, setBeneficiaryOfCompany] = useState([]);
  const [newUserId, setNewUserId] = useState("");

  const [pages, setPages] = useState({
    users: 0,
    requests: 0,
    campaigns: 0,
    partners: 0,
  });

  const pageSize = 15;

  const refreshAll = () => {
    const companyId = localStorage.getItem("companyId");

    getPartnershipRequests(companyId).then((res) => {
      setPartnerRequests(res.content || []);
    });

    getActivePartnerships(companyId).then((res) => {
      setActivePartners(res.content || []);
    });

    getInitiatedPartnerships(companyId).then((res) => {
      setInitiatedPartnerships(res.content || []);
    });

    getCompanyUsers(companyId).then((res) => {
      setUsers(res.content || []);
    });

    getInitiatedCampaigns(companyId).then((res) => {
      setInitiatedCampaigns(res.content || []);
    });

    getBeneficiaryOfCompany(companyId).then((res) => {
      setBeneficiaryOfCompany(res.content || []);
    });
  };

  useEffect(() => {
    refreshAll();
  }, [pages]);

  const handleRespond = async (requestId, accept) => {
    await respondToPartnership(requestId, accept);
    refreshAll();
  };

  const handleDeletePartnership = async (id) => {
    await deletePartnership(id);
    refreshAll();
  };

  const handleAddUser = async () => {
    if (!newUserId) return;
    console.log("fasfasfasfasfasfasf");
    await addUserToCompanyByUuid(localStorage.getItem("companyId"), newUserId);
    setNewUserId("");
    refreshAll();
  };

  const toggleUserStatus = async (user) => {
    if (user.active) await deactivateUser(user.id);
    else await activateUser(user.id);
    refreshAll();
  };

  const generateQRCode = (campaignId) => {
    console.log("Generate QR code for campaign:", campaignId);
  };

  return (
    <div className="p-6">
      <h2 className="text-2xl font-bold mb-6">Company Dashboard</h2>

      <div role="tablist" className="tabs tabs-boxed mb-6">
        <button
          className={`tab text-base font-medium px-6 py-2 ${
            activeTab === "partnerships"
              ? "tab-active bg-primary text-primary-content"
              : ""
          }`}
          onClick={() => setActiveTab("partnerships")}
        >
          Partnerships
        </button>
        <button
          className={`tab text-base font-medium px-6 py-2 ${
            activeTab === "users"
              ? "tab-active bg-primary text-primary-content"
              : ""
          }`}
          onClick={() => setActiveTab("users")}
        >
          Users
        </button>
        <button
          className={`tab text-base font-medium px-6 py-2 ${
            activeTab === "campaigns"
              ? "tab-active bg-primary text-primary-content"
              : ""
          }`}
          onClick={() => setActiveTab("campaigns")}
        >
          Campaigns
        </button>
      </div>

      {activeTab === "partnerships" && (
        <PartnershipTab
          partnerRequests={partnerRequests}
          activePartners={activePartners}
          pageSize={pageSize}
          pages={pages}
          setPages={setPages}
          handleRespond={handleRespond}
          handleDeletePartnership={handleDeletePartnership}
          formatDate={formatDate}
          onSendRequest={sendPartnershipRequest}
          initiatedPartnerships={initiatedPartnerships}
          refreshAll={refreshAll}
        />
      )}

      {activeTab === "users" && (
        <UserTab
          users={users}
          pageSize={pageSize}
          pages={pages}
          setPages={setPages}
          newUserId={newUserId}
          setNewUserId={setNewUserId}
          handleAddUser={handleAddUser}
          toggleUserStatus={toggleUserStatus}
        />
      )}

      {activeTab === "campaigns" && (
        <CampaignTab
          initiatedCampaigns={initiatedCampaigns}
          pageSize={pageSize}
          pages={pages}
          setPages={setPages}
          formatDate={formatDate}
          generateQRCode={generateQRCode}
          activePartners={activePartners}
        />
      )}
    </div>
  );
}
