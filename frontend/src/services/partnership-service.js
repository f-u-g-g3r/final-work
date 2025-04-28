import axios from "axios";

const API_URL = "http://localhost:8080/Partnership";

export async function getPartnershipRequests(
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
    filterOptions: [
      {
        filterBy: "status",
        filterValue: "pending",
      },
    ],
  }
) {
  try {
    const response = await axios.post(
      `${API_URL}/getPartnershipRequests/${companyId}`,
      filters,
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      }
    );
    console.log(response)
    return response.data;
  } catch (e) {
    return e;
  }
}

export async function getActivePartnerships(
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
    filterOptions: [
      {
        filterBy: "status",
        filterValue: "accepted",
      },
    ],
  }
) {
  try {
    const response = await axios.post(
      `${API_URL}/getPartnerships/${companyId}`,
      filters,
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      }
    );
    console.log(response)
    return response.data;
  } catch (e) {
    return e;
  }
}

export async function sendPartnershipRequest(companyId, partnerId) {
  try {
    console.log(companyId, partnerId)
    const response = await axios.post(
      `${API_URL}/request`,
      {
        requesterCompanyId: companyId,
        targetCompanyId: partnerId,
      },
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

export async function respondToPartnership(requestId, accept) {
  try {
    const response = await axios.post(
      `${API_URL}/respond`,
      {
        "partnershipId": requestId,
        "accepted": accept
      },
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

export async function deletePartnership(id) {
  try {
    const response = await axios.delete(`${API_URL}/${id}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });
    return response.data;
  } catch (e) {
    return e;
  }
}

export async function getInitiatedPartnerships(
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
    filterOptions: [
      {
        filterBy: "status",
        filterValue: "pending",
      },
    ],
  }
) {
  try {
    const response = await axios.post(
      `${API_URL}/getInitiatedPartnerships/${companyId}`,
      filters,
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      }
    );
    console.log(response)
    return response.data;
  } catch (e) {
    return e;
  }
}