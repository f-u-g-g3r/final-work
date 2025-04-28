import axios from "axios";

const API_URL = "http://localhost:8080/auth";

export async function auth(data) {
  try {
    return axios.post(`${API_URL}/login`, {
      email: data.email,
      password: data.password,
    });
  } catch (e) {
    return "Invalid credentials";
  }
}

export async function register(data) {
  try {
    return axios.post(`${API_URL}/register`, {
      name: data.name,
      email: data.email,
      password: data.password,
      companyId: data.companyId,
    });
  } catch (e) {
    return "Invalid credentials";
  }
}

export function isAuth() {
  return localStorage.getItem("token");
}
