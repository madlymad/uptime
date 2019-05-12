#!/usr/bin/env bash
# clearPrivate.sh
#
# Running this script will remove any private data from
# repository that should not commit in the public place

currentDir=$PWD

repo_dir=$(git rev-parse --git-dir)
cd ${repo_dir}/..

echo "Current location: `pwd`"

# Clear and replace any private data
rm -rf private
mkdir private
mv buildsystem/private/mock-secret.gradle private/secret.gradle

# Remove fastlane no reason to exist at the moment
rm -rf fastlane
rm Gemfile
rm Gemfile.lock

# Remove gitlab build script
rm .gitlab-ci.yml

# Other not used files
rm run.sh
rm -rf .idea

# Remove Play Store listing data
rm -rf app/src/main/play

cd ${currentDir}
