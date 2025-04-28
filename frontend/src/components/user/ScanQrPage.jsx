import React, { useEffect, useState, useRef } from "react";
import { useParams } from "react-router-dom";
import { resolveQr } from "../../services/qr-service";

export default function ScanQrPage() {
  const { uuid } = useParams();
  const [campaign, setCampaign] = useState(null);
  const [error, setError] = useState("");
  const calledRef = useRef(false);

  useEffect(() => {
    async function fetchQr() {
      if (calledRef.current) return;
      calledRef.current = true;

      try {
        const res = await resolveQr(uuid);
        console.log("Resolved QR Data:", res);
        setCampaign(res);
      } catch (e) {
        console.error("QR Error:", e);
        setError("QR expired, invalid, or already used.");
      }
    }
    fetchQr();
  }, [uuid]);

  if (error)
    return (
      <div className="p-6 text-center text-red-500">
        <h2 className="text-2xl font-bold mb-4">Invalid QR</h2>
        <p>{error}</p>
      </div>
    );

  if (!campaign) return <div className="p-6 text-center">Loading...</div>;

  return (
    <div className="p-6 space-y-4">
      <pre className="bg-base-200 p-4 rounded text-left overflow-x-auto">
        {JSON.stringify(campaign, null, 2)}
      </pre>
    </div>
  );
}
