import axios from "axios";

const API_URL = "http://localhost:8080/Company";

export async function registerCompany(data) {
  try {
    return axios.post(
      `${API_URL}`,
      {
        name: data.name,
        companyEmail: data.companyEmail,
        registrationCode: data.registrationCode,
      },
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      }
    );
  } catch (e) {
    return "Invalid credentials";
  }
}

export async function getActiveCompaniesMin() {
  try {
    const companies = await axios.get(`${API_URL}/getActiveCompaniesMin`);
    return companies.data;
  } catch (e) {
    return "Invalid credentials";
  }
}

export async function getCompanies(
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
    const companies = await axios.post(`${API_URL}/getCompanies`, filters, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });
    console.log(companies.data);
    return companies.data;
  } catch (e) {
    return "Invalid credentials";
  }
}

export async function activateCompany(id) {
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
    return "Invalid credentials";
  }
}

export async function deactivateCompany(id) {
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
    return "Invalid credentials";
  }
}

export async function getCompanyById(id) {
  try {
    const response = await axios.get(`${API_URL}/${id}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });
    console.log('companydata')
    console.log(response.data);
    return response.data;
  } catch (e) {
    return "Invalid credentials";
  }
}

export async function updateCompany(id, data) {
  console.log("updateCompany: ", data)
  try {
    const response = await axios.patch(`${API_URL}/updateCompany/${id}`, data, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });
    return response.data;
  } catch (e) {
    return e;
  }
}


export async function addUserToCompanyByUuid(companyId, uuid) {
  try {
    const response = await axios.post(
      `${API_URL}/addUserToCompanyByUuid`,
      {
        companyId: companyId,
        userUniqId: uuid,
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