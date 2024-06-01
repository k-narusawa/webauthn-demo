package com.knarusawa.webauthndemo.domain.credentials


enum class AAGUID(val aaguid: String, val labael: String) {
  GOOGLE_PASSWORD_MANAGER("ea9b8d66-4d01-1d21-3ce4-b6b48cb575d4", "Google Password Manager"),
  CHROME_ON_MAC("adce0002-35bc-c60a-648b-0b25f1f05503", "Chrome on Mac"),
  WINDOWS_HELLO("08987058-cadc-4b81-b6e1-30de50dcbe96", "Windows Hello"),
  WINDOWS_HELLO_1("9ddd1817-af5a-4672-a2b9-3e3dd95000a9", "Windows Hello"),
  WINDOWS_HELLO_2("6028b017-b1d4-4c02-b4b3-afcdafc96bb2", "Windows Hello"),
  ICLOUD_KEYCHAIN_MANAGED("dd4ec289-e01d-41c9-bb89-70fa845d4bf2", "iCloud Keychain (Managed)"),
  DASHLANE("531126d6-e717-415c-9320-3d9aa6981239", "Dashlane"),
  ONE_PASSWORD("bada5566-a7aa-401f-bd96-45619a55120d", "1Password"),
  NORDPASS("b84e4048-15dc-4dd0-8640-f4f60813c8af", "NordPass"),
  KEEPER("0ea242b4-43c4-4a1b-8b17-dd6d0b6baec6", "Keeper"),
  ENPASS("f3809540-7f14-49c1-a8b3-8f813b225541", "Enpass"),
  CHROMIUM_BROWSER("b5397666-4885-aa6b-cebf-e52262a439a2", "Chromium Browser"),
  EDGE_ON_MAC("771b48fd-d3d4-4f74-9232-fc157ab0507a", "Edge on Mac"),
  IDMELON("39a5647e-1853-446c-a1f6-a79bae9f5bc7", "IDmelon"),
  BITWARDEN("d548826e-79b4-db40-a3d8-11116f7e8349", "Bitwarden"),
  ICLOUD_KEYCHAIN("fbfc3007-154e-4ecc-8c0b-6e020557d7bd", "iCloud Keychain"),
  SAMSUNG_PASS("53414d53-554e-4700-0000-000000000000", "Samsung Pass"),
  THALES_BIO_IOS_SDK("66a0ccb3-bd6a-191f-ee06-e375c50b9846", "Thales Bio iOS SDK"),
  THALES_BIO_ANDROID_SDK("8836336a-f590-0921-301d-46427531eee6", "Thales Bio Android SDK"),
  THALES_PIN_ANDROID_SDK("cd69adb5-3c7a-deb9-3177-6800ea6cb72a", "Thales PIN Android SDK"),
  THALES_PIN_IOS_SDK("17290f1e-c212-34d0-1423-365d729f09d9", "Thales PIN iOS SDK"),
  PROTON_PASS("50726f74-6f6e-5061-7373-50726f746f6e", "Proton Pass"),
  KEEPASSXC("fdb141b2-5d84-443e-8a35-4698c205a502", "KeePassXC"),
  UNKNOWN("00000000-0000-0000-0000-000000000000", "Unknown")
  ;

  companion object {
    fun fromAAGUID(aaguid: String): AAGUID {
      return entries.find { it.aaguid == aaguid } ?: UNKNOWN
    }
  }

  override fun toString(): String {
    return """AAGUID(aaguid='$aaguid', labael='$labael')"""
  }
}