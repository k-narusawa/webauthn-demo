import { Button } from "@/components/commons/Button";
import { Card } from "@/components/commons/Card";
import { HorizontalLine } from "@/components/commons/HorizontalLine";

type Props = {
  passkeys: PasskeyResponse | null;
  onRegister: () => void;
  onDelete: (credential_id: number) => void;
};

export const PasskeyCard: React.FC<Props> = ({
  passkeys,
  onRegister,
  onDelete,
}) => {
  return (
    <>
      <Card>
        <div className="p-4 flex justify-center">
          <span className="text-2xl font-semi-bold">Passkey</span>
        </div>
        {passkeys?.keys.map((passkey) => (
          <>
            <HorizontalLine />
            <div className="grid grid-cols-6 py-4 px-8">
              <div className="col-start-1 col-span-3">
                <span className="text-gray-500">{passkey.key_name}</span>
              </div>
              <div className="col-start-6">
                <span
                  className="text-red-500 hover:underline hover:cursor-pointer"
                  onClick={() => {
                    onDelete(passkey.credential_id);
                  }}
                >
                  DELETE
                </span>
              </div>
            </div>
          </>
        ))}
        <HorizontalLine />
        <div className="flex justify-center">

          <div className="p-4 w-48">
            <Button
              onClick={onRegister}
              variant="primary"
              size="default"
            >
              Register
            </Button>
          </div>
        </div>
      </Card>
    </>
  );
};