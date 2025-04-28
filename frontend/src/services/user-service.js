import axios from "axios";

const API_URL = "http://localhost:8080/User";

export async function getUserDataMin(userId) {
  try {
    const userData = await axios.get(`${API_URL}/getUserDataMin/${userId}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });
    return userData.data;
  } catch (e) {
    return e;
  }
}

export async function getUsers(
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
    const users = await axios.post(`${API_URL}/getUsers`, filters, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });
    return users.data;
  } catch (e) {
    return e;
  }
}

export async function activateUser(id) {
  try {
    const response = await axios.patch(
      `${API_URL}/activate/${id}`,
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

export async function deactivateUser(id) {
  try {
    const response = await axios.patch(
      `${API_URL}/deactivate/${id}`,
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

export async function getCompanyUsers(
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
    const users = await axios.post(
      `${API_URL}/getCompanyUsers/${companyId}`,
      filters,
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      }
    );
    return users.data;
  } catch (e) {
    return e;
  }
}