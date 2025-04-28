import React from "react";

export default function UserTab({
  users,
  pageSize,
  pages,
  setPages,
  newUserId,
  setNewUserId,
  handleAddUser,
  toggleUserStatus,
}) {
  return (
    <div>
      <h3 className="text-xl font-semibold mb-2">Company Users</h3>
      <div className="flex gap-2 mb-4">
        <input
          type="text"
          placeholder="User ID or UUID"
          className="input input-bordered w-full max-w-xs"
          value={newUserId}
          onChange={(e) => setNewUserId(e.target.value)}
        />
        <button className="btn btn-primary" onClick={handleAddUser}>Add User</button>
      </div>
      <table className="table w-full">
        <thead>
          <tr>
            <th>ID</th>
            <th>Full Name</th>
            <th>UUID</th>
            <th>Status</th>
            <th>Toggle</th>
          </tr>
        </thead>
        <tbody>
          {users.length > 0 ? (
            users.map((user) => (
              <tr key={user.id}>
                <td>{user.id}</td>
                <td>{user.name}</td>
                <td>{user.uuid}</td>
                <td>{user.active ? "Active" : "Inactive"}</td>
                <td>
                  <button className="btn btn-xs" disabled={user.role === "COMPANY_OWNER"} onClick={() => toggleUserStatus(user)}>
                    {user.active ? "Disable" : "Enable"}
                  </button>
                </td>
              </tr>
            ))
          ) : (
            <tr><td colSpan="5" className="text-center text-sm text-gray-500">No users found.</td></tr>
          )}
        </tbody>
      </table>
      {users.length >= pageSize && (
        <div className="flex justify-end mt-2 gap-2">
          <button className="btn btn-xs" onClick={() => setPages(p => ({ ...p, users: p.users - 1 }))} disabled={pages.users === 0}>Prev</button>
          <button className="btn btn-xs" onClick={() => setPages(p => ({ ...p, users: p.users + 1 }))}>Next</button>
        </div>
      )}
    </div>
  );
}
