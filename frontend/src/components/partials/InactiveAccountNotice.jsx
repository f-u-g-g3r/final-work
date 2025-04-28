import React from "react";

export default function InactiveAccountNotice() {
    return (
        <div className="flex justify-center mt-24 px-4">
          <div className="card bg-base-100 shadow-xl p-8 max-w-md text-center w-full">
            <h2 className="text-2xl font-bold text-error mb-4">Account Inactive</h2>
            <p className="text-base">
              Your account is currently inactive. Once your company manager activates it, you'll be able to access promotions.
            </p>
          </div>
        </div>
      );
}
