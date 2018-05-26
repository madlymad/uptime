#!/usr/bin/env bash

set +e

boot_anim=""
fail_counter=0
check_every=5
timeout_in_sec=500

function device_status {
  echo -e "\nDevice status point $1:"
  adb shell dumpsys power | \
    grep -w -e "Display Power" \
      -e mStayOn -e mIsPowered \
      -e mBootCompleted \
      -e mSystemReady -e mDisplayReady
}

adb devices
adb wait-for-device

device_status "1"

until [[ "$boot_anim" =~ "stopped" ]]; do
  boot_anim=`adb -e shell getprop init.svc.bootanim 2>&1 &`
  echo "Boot animation: $boot_anim"
  if [[ "$boot_anim" =~ "not found"
    || "$boot_anim" =~ "device not found"
    || "$boot_anim" =~ "no emulators found"
    || "$boot_anim" =~ "device offline"
    || "$boot_anim" =~ "running" ]]; then
    let "fail_counter += ${check_every}"
    echo "Waiting for emulator to start..."
    if [[ ${fail_counter} -gt ${timeout_in_sec} ]]; then
      echo "Timeout ($timeout_in_sec seconds) reached; failed to start emulator"
      jobs -l
      exit 1
    fi
  fi
  sleep ${check_every}
done

device_status "2"

while : ; do
  echo -ne "."
  sleep 2
  boot_complete=$(adb shell getprop sys.boot_completed | tr -d '\r')
  [[ "$boot_complete" != "1" ]] || break
done

device_status "3"

echo -ne "\nEmulator API Level: "
while ! adb shell getprop ro.build.version.sdk; do
  sleep 1
done

device_status "4"

echo -e "\nEmulator is ready!"
adb devices

device_status "5"

# Wake up the device
adb shell settings put global window_animation_scale 0
adb shell settings put global transition_animation_scale 0
adb shell settings put global animator_duration_scale 0
adb shell input keyevent 82 # Unlock
adb shell input keyevent 224 # Set screen ON no matter what

device_status "6"

exit 0
