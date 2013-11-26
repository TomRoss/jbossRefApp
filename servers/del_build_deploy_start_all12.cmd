call %~dp0del_all_dirs.cmd
call %~dp0del_all_deploy.cmd
call %~dp0build_deploy_all.cmd
cd /d %~dp0
call %~dp0start_all1.cmd
call %~dp0start_all2.cmd