#Publish my private repository code to GitHub

##Working on a new repository

###Step 1. Configure the GitHub in my repository

    cd scripts
    ./config.sh

###Step 2. Remove project private data

    cd scripts
    ./clearPrivate.sh

##Working on an existing repository

###Step 1. Merge latest master to GitHub

    cd scripts
    ./merge.sh

###Step 2. Validate commit message and push the code

    git push upstream master
