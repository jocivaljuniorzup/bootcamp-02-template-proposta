package br.com.zup.jocivaldias.proposal.entity.enums;

public enum DigitalWalletProvider {
    PAYPAL,
    SAMSUNGPAY;

    public static DigitalWalletProvider toEnum(String name) {
        if (name == null) {
            return null;
        }

        for (DigitalWalletProvider x : DigitalWalletProvider.values()) {
            if (name.equalsIgnoreCase(x.name())) {
                return x;
            }
        }

        return null;
    }

}
