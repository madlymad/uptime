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

repo_dir=$(git rev-parse --git-dir)
cd ${repo_dir}/..

echo "Current location: `pwd`"

git branch --set-upstream-to=${upBranch} ${localBranch}

currentBranch=$(git branch | grep \* | cut -d ' ' -f2-)

if [[ "${currentBranch}" != "${localBranch}" ]]; then
    git checkout ${localBranch}
fi

# Update local master from remote
git fetch
git pull

# Merge the origin/master into the github_master
git merge --squash --strategy-option theirs master -m "Merge to github" && echo "Squash complete"

sleep 2
git reset --soft ${upBranch} && echo "Soft reset to latest upstream"

# Clear any private data
./scripts/clearPrivate.sh && echo "Private data cleared"

# Git now considers all changes as un-staged changes.
# We can add these changes as one commit.
# Adding . will also add un-tracked files.
git add --all  && echo "Everything is added please add a commit message!"

sleep 2
git commit && echo -e "\nFor push use:\n\n\t git push upstream github_master:master"


cd ${currentDir}
