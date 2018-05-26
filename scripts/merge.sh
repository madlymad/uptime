#!/usr/bin/env bash
# merge.sh
#
# https://makandracards.com/makandra/527-squash-several-git-commits-into-a-single-commit
# Running this script will merge all master commits in
# a single commit of upstream master branch

currentDir=$PWD

fromBranch=master
upBranch=upstream/master
localBranch=github_master

git branch --set-upstream-to=${upBranch} ${localBranch}

currentBranch=$(git branch | grep \* | cut -d ' ' -f2-)

if [[ "${currentBranch}" != "${localBranch}" ]]; then
    git checkout ${localBranch}
fi

# Update local master from remote
git fetch
git pull

# Merge the origin/master into the github_master
git merge master

# Reset the github_master to its upstream state
git reset upstream/master

# Git now considers all changes as unstaged changes.
# We can add these changes as one commit.
# Adding . will also add untracked files.
git add --all
git commit


cd ${currentDir}
