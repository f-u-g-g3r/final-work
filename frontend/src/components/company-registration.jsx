import { Form, redirect } from "react-router-dom";
import { registerCompany } from "../services/company-service";

export async function registerCompanyAction({ request }) {
  const formData = await request.formData();
  const companyData = Object.fromEntries(formData);

  try {
    const response = await registerCompany(companyData);
    console.log(response);

    if (response.status !== 201) {
      console.log("Error registering company:", response.data);
      return redirect("/home");
    } else {
      return location.reload();
    }
  } catch (e) {
    console.log(e);

    return null;
  }
}

export default function CompanyRegistration() {
  return (
    <div className="flex justify-center items-center">
      <div className="card w-96 bg-base-100 p-6">
        <Form method="post">
          <div className="card-body">
            <div className="form-control text-left">
              <label className="label">
                <span className="label-text block text-left">Company name</span>
              </label>
              <input
                type="text"
                placeholder="Enter company name"
                className="input input-bordered w-full placeholder-gray-500"
                name="name"
                required
              />
            </div>
            <div className="form-control mt-2 text-left">
              <label className="label">
                <span className="label-text block text-left">
                  Company email
                </span>
              </label>
              <input
                type="email"
                placeholder="Enter company email"
                className="input input-bordered w-full placeholder-gray-500"
                name="companyEmail"
                required
              />
            </div>
            <div className="form-control mt-2 text-left">
              <label className="label">
                <span className="label-text block text-left">
                  Registration code
                </span>
              </label>
              <input
                type="text"
                placeholder="Enter registration code"
                className="input input-bordered w-full placeholder-gray-500"
                name="registrationCode"
                required
              />
            </div>
            <div className="form-control mt-4">
              <button className="btn btn-primary">Register</button>
            </div>
          </div>
        </Form>
      </div>
    </div>
  );
}
