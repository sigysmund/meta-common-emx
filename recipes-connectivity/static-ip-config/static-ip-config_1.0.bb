# -------------------
# Configures the system to use a static IP
# -------------------

# Depends on the following custom env vars exported to the yocto build:
# - NETWORK_STATIC_IP

SUMMARY = "Configures the system to use a static IP."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI += " \
    file://eth.network.j2 \
    file://en.network.j2 \
    file://wlan.network.j2 \
"

FILES:${PN} += " \
    ${sysconfdir}/systemd/network/* \
"

RDEPENDS:${PN} += "systemd"

inherit templating

S = "${WORKDIR}/sources-unpack"

TEMPLATE_FILES = "\
    ${S}/en.network.j2 \
    ${S}/eth.network.j2 \
    ${S}/wlan.network.j2 \
"

python do_patch() {
    static_ip =  d.getVar('NETWORK_STATIC_IP', True)
    if static_ip is None:
        bb.error("No static IP configured, but package 'system-static-ip' included!")

    params = {
        "static_ip": static_ip
    }
    files = d.getVar('TEMPLATE_FILES', True).split()
    for file in files:
        render_template(file, params)
}

do_install() {
    install -d ${D}${sysconfdir}/systemd/network
    install -m 0644 ${S}/eth.network ${D}${sysconfdir}/systemd/network
    install -m 0644 ${S}/en.network ${D}${sysconfdir}/systemd/network
    install -m 0644 ${S}/wlan.network ${D}${sysconfdir}/systemd/network
}
