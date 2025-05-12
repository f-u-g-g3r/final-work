import axios from "axios";

const API_URL = "http://localhost:8080/Campaign";

export async function getInitiatedCampaigns(
  companyId,
  filters = {
    pagingOptions: {
      page: 0,
      pageSize: 5,
    },
    sortOptions: {
      sortBy: "id",
      direction: 0,
    },
  }
) {
  try {
    const campaigns = await axios.post(
      `${API_URL}/initiatedByCompany/${companyId}`,
      filters,
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      }
    );
    return campaigns.data;
  } catch (e) {
    return e;
  }
}

export async function getBeneficiaryOfCompany(
  companyId,
  filters = {
    pagingOptions: {
      page: 0,
      pageSize: 15,
    },
    sortOptions: {
      sortBy: "id",
      direction: 0,
    },
  }
) {
  try {
    const campaigns = await axios.post(
      `${API_URL}/beneficiaryOfCompany/${companyId}`,
      filters,
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      }
    );
    return campaigns.data;
  } catch (e) {
    return e;
  }
}

export async function getCampaignById(id) {
  try {
    const campaign = await axios.get(`${API_URL}/getCampaign/${id}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });
    return campaign.data;
  } catch (e) {
    return e;
  }
}

export async function createCampaign(campaign) {
  console.log(campaign)
  try {
    const response = await axios.post(`${API_URL}`, campaign, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });
    return response.data;
  } catch (e) {
    return e;
  }
}

export async function updateCampaign(id, campaign) {
  try {
    const response = await axios.patch(`${API_URL}/${id}`, campaign, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });
    return response.data;
  } catch (e) {
    return e;
  }
}

export async function deleteCampaign(id) {
  try {
    const response = await axios.patch(
      `${API_URL}/delete/${id}`,
      {},
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      }
    );
    return response.data;
  } catch (e) {
    return e;
  }
}
