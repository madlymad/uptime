#!/usr/bin/env bash
# config.sh
#
# Running this script will configure a github upstream
# that is based on gitlab existing origin configuration
#
# To initialize the local upstream/master we should have
# created a new project in github first.


currentDir=$PWD

# Default values
verbose=false
force=false

# read bash options
while getopts 'fv' flag; do
  case "${flag}" in
    f) force=true ;;
    v) verbose=true ;;
    *) error "Unexpected option ${flag}" ;;
  esac
done

repo_name=$(basename -s .git `git config --get remote.origin.url`)
${verbose} && echo -e "Repository Name: ${repo_name}"
repo_dir=$(git rev-parse --git-dir)
${verbose} && echo -e "Repository Directory ${repo_dir}\n"

cd ${repo_dir}/..

githubConfig=$(git remote -v | grep github | wc -l)

if [[ "${githubConfig}" == "0" || ${force} ]]; then
    if [[ ${force} && "${githubConfig}" != "0" ]]; then
        echo -e "Clean existing upstream"
        git remote remove upstream
    else
        #${verbose} &&
        echo -e "Remote configuration for github does not exist"
    fi

    # git@gitlab.com:madlymad/uptime.git
    origin=$(git config --get remote.origin.url)
    if [[ ! -z ${origin} ]]; then
        # I have the same username and same repository name in gitlab and github
        upstream=$(echo ${origin} | sed 's/gitlab/github/g' )
        ${verbose} && echo -e "Configure upstream with ${upstream}\n"
        git remote add upstream ${upstream}
    fi
fi

echo -e "Remote configuration is:\n"
git remote -v

git fetch upstream
masterExists=$(git branch -r | grep upstream | grep master | wc -l)
if [[ "${masterExists}" == "0" ]]; then
    # This should be an initial state even for remote
    git checkout --orphan github_master
    echo -e "\nPlease run the command:\n\n\t ./scripts/clearPrivate.sh"
    echo -e "\nThe you can add commit and push your data to upstream.\n"
    echo -e "\nFor push use:\n\n\t git push -u upstream github_master:master"
fi

cd ${currentDir}