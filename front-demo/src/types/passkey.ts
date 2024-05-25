type PasskeyResponse = {
  keys: PasskeyResponseItem[];
};

type PasskeyResponseItem = {
  credential_id: number;
  id: string;
  aaguid: string;
  label: string;
};