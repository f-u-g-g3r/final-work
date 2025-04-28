import { Form, Link, redirect } from "react-router-dom";
import { auth, isAuth } from "../../services/auth-service";

export async function action({ request }) {
  const formData = await request.formData();
  const userData = Object.fromEntries(formData);

  try {
    const { data } = await auth(userData);

    localStorage.setItem("token", data.token);
    localStorage.setItem("authority", data.role);
    localStorage.setItem("uuid", data.uuid);
    localStorage.setItem("userId", data.id);
    if (data.role === "ADMIN") {
      return redirect("/admin-panel");
    } else {
      return redirect("/home");
    }
  } catch (e) {
    return redirect("/login");
  }
}

export function loginLoader() {
  if (isAuth()) {
    return redirect("/home");
  }
  return null;
}

export default function Login() {
  return (
    <div className="flex justify-center items-center min-h-screen bg-base-200">
      <div className="card w-96 bg-base-100 shadow-xl p-6">
        <h2 className="text-2xl font-bold text-center">Login</h2>
        <Form method="post">
          <div className="card-body">
            <div className="form-control text-left">
              <label className="label">
                <span className="label-text">Email</span>
              </label>
              <input
                type="email"
                placeholder="Enter your email"
                className="input input-bordered"
                name="email"
                required
              />
            </div>
            <div className="form-control text-left mt-2">
              <label className="label">
                <span className="label-text">Password</span>
              </label>
              <input
                type="password"
                placeholder="Enter your password"
                className="input input-bordered"
                name="password"
                required
              />
            </div>
            <div className="form-control mt-4 text-center">
              <button className="btn btn-primary">Login</button>
            </div>
            <div className="text-center mt-2">
              <Link to={`/sign-up`} className="text-sm text-primary">
                Sign up
              </Link>
            </div>
          </div>
        </Form>
      </div>
    </div>
  );
}
