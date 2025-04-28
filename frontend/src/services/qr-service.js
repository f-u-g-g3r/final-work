import axios from "axios";

const API_URL = "http://localhost:8080/Qr";

export async function generateQr(campaignId) {
  try {
    const response = await axios.post(
      `${API_URL}/generate`,
      {
        campaignId: campaignId,
        ttlSeconds: 3600, // QR code valid for 1 hour
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

export async function resolveQr(uuid) {
    try {
      const response = await axios.get(`${API_URL}/resolve/${uuid}`);
      console.log('response: ', response.data);
      return response.data;
    } catch (e) {
      console.error("Error while resolving QR:", e);
      throw e;
    }
  }
  