import { Button } from "@/components/commons/Button";
import { Card } from "@/components/commons/Card";
import { FormEventHandler } from "react";

type LoginCardProps = {
  handleLogin: FormEventHandler<HTMLFormElement>;
  handleWebAuthnLogin: FormEventHandler<HTMLFormElement>;
}

const LoginCard: React.FC<LoginCardProps> = ({
  handleLogin,
  handleWebAuthnLogin,
}) => {
  return (
    <Card >
      <div className="p-4">
        <div className="text-center font-bold font-sans text-xl pb-4">
          Login
        </div>
        <form onSubmit={handleLogin}>
          <div className="mb-6">
            <label
              htmlFor="email"
              className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
            >
              Username
            </label>
            <input
              type="email"
              id="username"
              name="username"
              defaultValue="test@example.com"
              className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 "
              required
            />
          </div>
          <div className="mb-6">
            <label
              htmlFor="password"
              className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
            >
              Password
            </label>
            <input
              type="password"
              id="password"
              name="password"
              defaultValue="!Password0"
              className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 "
              required
            />
          </div>
          <div className="px-12">
            <Button
              type="submit"
              variant="primary"
              disabled={false}
            >
              Login
            </Button>
          </div>
        </form>
        <div className="p-4" />
        <form onSubmit={handleWebAuthnLogin}>
          <div className="px-12">
            <Button
              type="submit"
              variant="primary"
              disabled={false}
            >
              WebAuthn Login
            </Button>
          </div>
        </form>
      </div>
    </Card>
  );
};

export default LoginCard;
