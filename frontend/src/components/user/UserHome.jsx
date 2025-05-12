import { useEffect, useState } from "react";
import { getBeneficiaryOfCompany } from "../../services/campaign-service";
import QRCode from "react-qr-code";
import { generateQr } from "../../services/qr-service";
import { formatDate } from "../../utils/utils";

export default function UserHome() {
  const [campaigns, setCampaigns] = useState([]);
  const [qrUrls, setQrUrls] = useState({});

  useEffect(() => {
    getBeneficiaryOfCompany(localStorage.getItem("companyId")).then((res) => {
      setCampaigns(res.content || []);
    });
  }, []);

  const handleGenerateQR = async (campaignId) => {
    try {
      const res = await generateQr(campaignId);
      setQrUrls((prev) => ({ ...prev, [campaignId]: res.qrUrl }));
    } catch (error) {
      console.error("Failed to generate QR:", error);
    }
  };

  return (
    <div className="p-8 space-y-10">
      <h2 className="text-3xl font-bold text-center mb-8">
        Available Campaigns
      </h2>

      {campaigns.length > 0 ? (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          {campaigns.map((c) => (
            <div
              key={c.id}
              className="card bg-base-100 border border-base-300 hover:shadow-2xl transition-all duration-300 ease-in-out p-6 flex flex-col justify-between space-y-6 rounded-md"
            >
              <div className="flex flex-col gap-2">
                <h3 className="text-xl font-extrabold text-primary">
                  {c.title}
                </h3>
                <p className="text-gray-700">{c.description}</p>
                <p className="text-sm text-gray-500 mt-2">
                  <span className="font-medium">Valid until:</span> {formatDate(c.validUntil) || "∞"}
                </p>
              </div>

              <div className="mt-6 flex flex-col items-center gap-4">
                <button
                  className="btn btn-outline btn-primary w-full transition-all duration-200"
                  onClick={() => handleGenerateQR(c.id)}
                >
                  Generate QR Code
                </button>

                {qrUrls[c.id] && (
                  <div className="bg-base-200 p-4 rounded-md shadow-inner w-full flex justify-center">
                    <QRCode value={qrUrls[c.id]} size={140} />
                  </div>
                )}
              </div>
            </div>
          ))}
        </div>
      ) : (
        <div className="text-center text-gray-500">
          No available campaigns yet.
        </div>
      )}
    </div>
  );
}
