SUMMARY = "meshtasticd firmware daemon"
DESCRIPTION = "meshtasticd is the firmware daemon for Meshtastic"
HOMEPAGE = "https://github.com/meshtastic/firmware"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=8f0e2cd40e05189ec81232da84bd6e1a"

SRC_URI = "git://github.com/meshtastic/firmware.git;protocol=https;branch=master;tag=v2.5.21.447533a;submodules=1"

# Where to find the source once fetched
S = "${WORKDIR}/git"

DEPENDS += "\
    pkgconf \
    zlib \
    openssl-native \
    python3-platformio-native \
    libgpiod yaml-cpp bluez5 i2c-tools libusb1 \
"

RDEPENDS:${PN} += " \
    openssl \
    libgpiod \
    yaml-cpp \
    zlib \
    bluez5 \
    i2c-tools \
    libusb1 \
"

do_compile () {
    # Export the target tools and flags as in Buildroot.
    export TARGET_AR="${TARGET_AR}"
    export TARGET_AS="${TARGET_AS}"
    export TARGET_CC="${TARGET_CC}"
    export TARGET_CXX="${TARGET_CXX}"
    export TARGET_LDFLAGS="${TARGET_LDFLAGS}"
    export TARGET_CFLAGS="${TARGET_CFLAGS} -I${STAGING_DIR_TARGET}/usr/include"
    export TARGET_CXXFLAGS="${TARGET_CXXFLAGS} -I${STAGING_DIR_TARGET}/usr/include"
    export TARGET_LD="${TARGET_LD}"
    export TARGET_OBJCOPY="${TARGET_OBJCOPY}"
    export TARGET_RANLIB="${TARGET_RANLIB}"
    
    # Set up PlatformIO cache directories as needed.
    export PLATFORMIO_CACHE_DIR="${WORKDIR}/.platformio_cache"
    export PLATFORMIO_BUILD_CACHE_DIR="${WORKDIR}/.platformio_build_cache"
    
    # Call the PlatformIO build command.
    ${STAGING_BINDIR_NATIVE}/python3 -m platformio run --environment native --project-dir ${S}
}

do_install () {
    # Create directories
    install -d ${D}${sbindir}
    install -d ${D}${sysconfdir}/meshtasticd/config.d
    install -d ${D}${sysconfdir}/meshtasticd/available.d
    
    # Install the meshtasticd binary
    install -D -m 0755 ${S}/.pio/build/native/program ${D}${sbindir}/meshtasticd
    
    # Install configuration files
    install -D -m 0644 ${S}/bin/config-dist.yaml ${D}${sysconfdir}/meshtasticd/config.yaml
    install -d ${D}${sysconfdir}/meshtasticd/available.d
    cp -r ${S}/bin/config.d/* ${D}${sysconfdir}/meshtasticd/available.d/
    cp -r ${S}/config.d/* ${D}${sysconfdir}/meshtasticd/available.d/
    
    # Optionally install service files (choose the proper scheme based on your init system)
    # For a SysV init script:
    install -d ${D}${sysconfdir}/init.d
    install -D -m 0755 ${S}/S99meshtasticd ${D}${sysconfdir}/init.d/meshtasticd
    install -D -m 0755 ${S}/meshtasticd-syslog-wrapper.sh ${D}${libexecdir}/meshtasticd-syslog-wrapper.sh
    
    # If you are using systemd, you might also install a service file:
    # install -d ${D}${systemd_unitdir}/system
    # install -D -m 0644 ${S}/meshtasticd.service ${D}${systemd_unitdir}/system/meshtasticd.service
}

