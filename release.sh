#!/usr/bin/env bash

set -e
set -u
set -o pipefail


if [[ -z "${1-}" ]]; then
    echo "Missing parameter version">&2
    exit 1
fi

version="$1"

export PUBLISH_VERSION="${version}"

ensure_clean () {
  dirty=$(git status --porcelain)
  if [ "${dirty}" ]; then
      echo "Cannot release because these files are dirty:"
      echo "${dirty}"
      exit 1
  fi >&2
}

find . -name target -prune -exec rm -r {} \;

sed -i "" -E "s%(.*)<version>[^<]*</version>%\1<version>${version}</version>%g" jsonmatch/test/src/jsonmatch/MatchingTest.java

./mill jsonmatch.test

cp README.md pages/index.md

ensure_clean # README.md and MatchingTest.java still clean?


echo "Releasing version ${version}"

tag="v${version}"

previous_release_tag=$( git describe --abbrev=0 || git rev-list HEAD | tail -n 1 )

git log ${previous_release_tag}..HEAD > /tmp/release-notes-candidate.txt

vim /tmp/release-notes-candidate.txt

release_notes=$(cat /tmp/release-notes-candidate.txt)

mill jsonmatch.publishSonatypeCentral

git tag -f "${tag}" --annotate --message "${release_notes}"

git push origin tag "${tag}"
