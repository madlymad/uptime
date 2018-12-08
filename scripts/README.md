# Publish my private repository code to GitHub

*This documentation can used at your own repository if you want to publish something on GitHub after
deleting any private/own data*

------------------------------------------------------

## Working on a new repository

### Step 1. Configure the GitHub in my repository

    cd scripts
    ./config.sh

### Step 2. Remove project private data

    cd scripts
    ./clearPrivate.sh

### Step 3. Add and commit

    git add .
    git commit -m "Initial commit"

### Step 4. Push to upstream

    git push -u upstream github_master:master

------------------------------------------------------

## Working on an existing repository

### Step 1. Merge latest master to GitHub

    cd scripts
    ./merge.sh

### Step 2. Validate commit message and push the code

    git push upstream github_master:master
