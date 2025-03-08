test:
push_sp
set_bp
push 0
push 0
push 1
st
push 0
ld
end
main:
push_sp
set_bp
call test
