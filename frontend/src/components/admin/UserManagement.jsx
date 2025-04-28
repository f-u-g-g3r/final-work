import React, { useEffect, useState } from "react";

import { getUsers, activateUser, deactivateUser } from "../../services/user-service";
export default function UserManagement() {
  const [users, setUsers] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [sortBy, setSortBy] = useState("id");
  const [direction, setDirection] = useState(0);
  const [search, setSearch] = useState("");

  useEffect(() => {
    const request = {
      pagingOptions: {
        page: page,
        pageSize: 15,
      },
      sortOptions: {
        sortBy,
        direction,
      },
    };

    if (search.trim() !== "") {
      request.filterOptions = [
        {
          filterBy: "generalSearch",
          filterValue: search,
        },
      ];
    }

    getUsers(request).then((res) => {
      setUsers(res.content);
      setTotalPages(res.page.totalPages);
    });
  }, [page, sortBy, direction, search]);

  const handleToggleStatus = async (id, isActive) => {
    try {
      if (isActive) {
        await deactivateUser(id);
      } else {
        await activateUser(id);
      }
      setUsers((prev) =>
        prev.map((user) =>
          user.id === id ? { ...user, active: !user.active } : user
        )
      );
    } catch (e) {
      console.error("Failed to toggle user status", e);
    }
  };

  const handleSort = (field) => {
    if (sortBy === field) {
      setDirection((prev) => (prev === 0 ? 1 : 0));
    } else {
      setDirection(0);
      setSortBy(field);
    }
  };

  return (
    <div className="p-6">
      <h3 className="text-xl font-bold mb-4">Manage Users</h3>

      <div className="mb-4">
        <input
          type="text"
          placeholder="Search..."
          className="input input-bordered w-full max-w-xs"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
      </div>

      <div className="overflow-x-auto">
        <table className="table table-zebra w-full">
          <thead>
            <tr>
              <th onClick={() => handleSort("id")} className="cursor-pointer">
                ID {sortBy === "id" ? (direction === 0 ? "▼" : "▲") : ""}
              </th>
              <th onClick={() => handleSort("name")} className="cursor-pointer">
                Name {sortBy === "name" ? (direction === 0 ? "▼" : "▲") : ""}
              </th>
              <th>
                Company (ID)
              </th>
              <th onClick={() => handleSort("email")} className="cursor-pointer">
                Email {sortBy === "email" ? (direction === 0 ? "▼" : "▲") : ""}
              </th>
              <th onClick={() => handleSort("role")} className="cursor-pointer">
                Role {sortBy === "role" ? (direction === 0 ? "▼" : "▲") : ""}
              </th>
              <th>UUID</th>
              <th onClick={() => handleSort("isActive")} className="cursor-pointer">
                Status {sortBy === "isActive" ? (direction === 0 ? "▼" : "▲") : ""}
              </th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.id}>
                <td className="w-[60px] whitespace-nowrap">{user.id}</td>
                <td className="max-w-[300px] w-[200px] truncate whitespace-nowrap">{user.name}</td>
                <td className="max-w-[200px] w-[200px] truncate whitespace-nowrap">{user.companyName ? `${user.companyName} (${user.companyId})` : null} </td>
                <td className="max-w-[300px] w-[300px] truncate whitespace-nowrap">{user.email}</td>
                <td className="max-w-[200px] w-[200px] truncate whitespace-nowrap">{user.role}</td>
                <td className="max-w-[200px] w-[200px] truncate whitespace-nowrap">{user.uuid}</td>
                <td>
                  <span className={user.active ? "text-success" : "text-error"}>
                    {user.active ? "Active" : "Inactive"}
                  </span>
                </td>
                <td className="space-x-2">
                  <button
                    className="btn btn-xs btn-outline"
                    disabled={user.role === 'ADMIN'}
                    onClick={() => handleToggleStatus(user.id, user.active)}
                  >
                    {user.active ? "Deactivate" : "Activate"}
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Pagination Controls */}
      <div className="flex justify-center mt-4 gap-2">
        <button
          className="btn btn-sm"
          disabled={page === 0}
          onClick={() => setPage((p) => p - 1)}
        >
          Prev
        </button>
        <span className="text-sm pt-2">
          Page {page + 1} of {totalPages}
        </span>
        <button
          className="btn btn-sm"
          disabled={page + 1 >= totalPages}
          onClick={() => setPage((p) => p + 1)}
        >
          Next
        </button>
      </div>
    </div>
  );
}