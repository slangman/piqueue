package kz.hustle.equeue.entity;

public enum Voice {
    cmu_rms_hsmm ("cmu-rms-hsmm", "Henry (M)"),
    cmu_slt_hsmm ("cmu-slt-hsmm", "Sally (F)"),
    dfki_poppy_hsmm ("dfki-poppy-hsmm", "Poppy (F)");

    private String voiceName;

    private String title;

    Voice(String voiceName, String title) {
        this.voiceName = voiceName;
        this.title = title;
    }

    public String getVoiceName() {
        return voiceName;
    }

    public String getTitle() {
        return title;
    }
}
