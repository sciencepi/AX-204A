@echo off
echo # AX-204A >> README.md
git init
git add .
git commit -m "first commit"
git branch -M main
git remote add origin git@github.com:sciencepi/AX-204A.git
git push -u origin main