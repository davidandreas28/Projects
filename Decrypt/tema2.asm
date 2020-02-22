extern puts
extern printf
extern strlen

%define BAD_ARG_EXIT_CODE -1

section .data
filename: db "./input0.dat", 0
inputlen: dd 2263

fmtstr:            db "Key: %d",0xa, 0
usage:             db "Usage: %s <task-no> (task-no can be 1,2,3,4,5,6)", 10, 0
error_no_file:     db "Error: No input file %s", 10, 0
error_cannot_read: db "Error: Cannot read input file %s", 10, 0

section .text
global main

xor_strings:
	; TODO TASK 1
        XOR EAX, EAX
        XOR EBX, EBX
        XOR EDX, EDX

xor_string_loop:
        mov al, byte [ecx + edx] ; primul caracter
        cmp eax, 0x00
        je getout
        mov bl, byte [edi + edx] ; al doilea caracter
        xor al, bl
        mov byte[ecx + edx], al ; punem in string-ul din input caracterul decodificat
        inc edx
        jmp xor_string_loop
getout:
	ret

rolling_xor:
	; TODO TASK 2
        XOR EAX, EAX
        XOR EBX, EBX
        XOR EDX, EDX
        mov al, byte [ecx] ; primul caracter
rolling_xor_loop:
        inc ebx
        mov dl, byte [ecx + ebx] ; urmatorul caracter
        cmp dl, 0x00 ; am ajuns la sfarsit?
        je getout 
        xor dl, al
        mov al, byte [ecx + ebx] ; tinem minte caracterul de pe pozitia curenta
        mov byte [ecx + ebx], dl ; punem in string-ul din input caracterul decodificat 
        jmp rolling_xor_loop 
        

xor_hex_strings:
	; TODO TASK 3
        xor eax, eax
        xor ebx, ebx
        xor edx, edx
        xor esi, esi
form_first_string:
        xor eax, eax
        xor edx, edx
        mov al, byte [ecx + ebx] ; luam cate doua caractere
        mov dl, byte [ecx + ebx + 1]
        cmp al, 0x00
        je  formed_first_string ; formam string-ul codificat
        jmp form_al
        
form_al:
        cmp al, '9'
        jg al_is_letter
        sub al, '0'
        shl al, 4
        cmp dl, '9'
        jg dl_is_letter
        sub dl, '0'
        add al, dl
        jmp put_in_ecx
al_is_letter:
        sub al, 'a'
        add al, 10
        shl al, 4
        cmp dl, '9'
        jg dl_is_letter
        sub dl, '0'
        add al, dl
        jmp put_in_ecx
dl_is_letter:
        sub dl, 'a'
        add dl, 10
        add al, dl
        jmp put_in_ecx
put_in_ecx:
        mov byte [ecx + esi], al
        inc esi
        add ebx, 2
        jmp form_first_string       
       
        ; am format string-ul codificat
formed_first_string:
        mov byte [ecx + esi], 0x00
        xor ebx, ebx
        xor esi, esi
        jmp form_second_string ; formam cheia
        
        
form_second_string:
        xor eax, eax
        xor edx, edx
        mov al, byte [edi + ebx]
        mov dl, byte [edi + ebx + 1]
        cmp al, 0x00
        je  formed_second_string
        jmp form_al_2
                
form_al_2:
        cmp al, 57
        jg al_2_is_letter
        sub al, '0'
        shl al, 4
        cmp dl, 57
        jg dl_2_is_letter
        sub dl, '0'
        add al, dl
        jmp put_in_edi
        
al_2_is_letter:
        sub al, 'a'
        add al, 10
        shl al, 4
        cmp dl, 57
        jg dl_2_is_letter
        sub dl, '0'
        add al, dl
        jmp put_in_edi
dl_2_is_letter:
        sub dl, 'a'
        add dl, 10
        add al, dl
        jmp put_in_edi

put_in_edi:
        mov byte [edi + esi], al
        inc esi
        add ebx, 2
        jmp form_second_string


formed_second_string:
        mov byte [edi + esi], 0x00
        jmp xor_strings

base32decode:
	; TODO TASK 4
	ret

bruteforce_singlebyte_xor:
	; TODO TASK 5
	
        xor edx,edx
        xor ebx,ebx
        xor eax,eax
        mov al, 0x00
start_checking:        
        mov bl, byte[ecx + edx]
        cmp bl,0x00
        je start_again ; am terminat sirul si n-am gasit cheia... mai incercam
        xor bl,al
        ; merg dintr-un label in altul pana formez cuvantul 'force'
check_key:
        cmp bl, 'f' ; e 'f'?
        jne goback

        mov bl,byte[ecx + edx + 1]
        xor bl,al      
        cmp bl, 'o'  ; e 'o'?
        jne goback

        mov bl,byte[ecx + edx + 2]
        xor bl,al  
        cmp bl, 'r'  ; e 'r'?
        jne goback

        mov bl,byte[ecx + edx + 3]
        xor bl,al    
        cmp bl, 'c'   ; e 'c'?
        jne goback

        mov bl,byte[ecx + edx + 4]
        xor bl,al
        cmp bl, 'e'  ; e 'e'?
        jne goback
          
        xor ebx,ebx
        xor edx,edx
        ; am gasit cheia, facem decodificarea
xor_string:        
        mov bl, byte[ecx + edx]
        cmp bl,0x00
        je getout
        xor bl,al
        mov byte[ecx + edx],bl
        inc edx
        jmp xor_string
start_again:
        xor edx, edx 
        inc eax
        jmp start_checking     
        
goback:
        inc edx 
        jmp start_checking

decode_vigenere:
	; TODO TASK 6
        
        xor ebx, ebx
        xor edx, edx
        xor edi,edi
        
decode:
        mov dl, byte [ecx + edi]
        cmp dl, 0x00
        je exit_1
        cmp dl, 97 ; comparam cu 'a'
        jl continue
        cmp dl, 122 ; comparam cu 'z'
        jg continue
        jmp is_Char
continue:
        inc edi
        xor edx, edx
        jmp decode
is_Char:
        mov dh, byte [eax + ebx]
        cmp dh, 0x00
        je reiterate ; o luam de la inceput cu cheia 
        sub dh, 'a' 
        sub dl, dh ; decodificam
        cmp dl, 97 ; comparam cu 'a'
        jl below_A ; inseamna ca am iesit din alfabet
        jmp continue_1
below_A:
        add dl, 26 ; adunam cate litere sunt in alfabet
        jmp continue_1
continue_1:
    mov byte [ecx + edi], dl
    inc ebx
    inc edi
    xor edx, edx
    jmp decode
             
reiterate:
    xor ebx, ebx
    
    jmp is_Char
exit_1:
    
    ret
main:
    mov ebp, esp; for correct debugging
	push ebp
	mov ebp, esp
	sub esp, 2300

	; test argc
	mov eax, [ebp + 8]
	cmp eax, 2
	jne exit_bad_arg

	; get task no
	mov ebx, [ebp + 12]
	mov eax, [ebx + 4]
	xor ebx, ebx
	mov bl, [eax]
	sub ebx, '0'
	push ebx

	; verify if task no is in range
	cmp ebx, 1
	jb exit_bad_arg
	cmp ebx, 6
	ja exit_bad_arg

	; create the filename
	lea ecx, [filename + 7]
	add bl, '0'
	mov byte [ecx], bl

	; fd = open("./input{i}.dat", O_RDONLY):
	mov eax, 5
	mov ebx, filename
	xor ecx, ecx
	xor edx, edx
	int 0x80
	cmp eax, 0
	jl exit_no_input

	; read(fd, ebp - 2300, inputlen):
	mov ebx, eax
	mov eax, 3
	lea ecx, [ebp-2300]
	mov edx, [inputlen]
	int 0x80
	cmp eax, 0
	jl exit_cannot_read

	; close(fd):
	mov eax, 6
	int 0x80

	; all input{i}.dat contents are now in ecx (address on stack)
	pop eax
	cmp eax, 1
	je task1
	cmp eax, 2
	je task2
	cmp eax, 3
	je task3
	cmp eax, 4
	je task4
	cmp eax, 5
	je task5
	cmp eax, 6
	je task6
	jmp task_done

task1:
	; TASK 1: Simple XOR between two byte streams

	; TODO TASK 1: find the address for the string and the key
	; TODO TASK 1: call the xor_strings function
         
         push ecx
	call strlen
	pop ecx

	add eax, ecx
	inc eax
        
        mov edi, eax
	push edi
         ;push edi
	push ecx
        call xor_strings
	call puts                   ;print resulting string
	add esp, 4

	jmp task_done

task2:
	; TASK 2: Rolling XOR
        
	; TODO TASK 2: call the rolling_xor function
        
        
	push ecx
        call rolling_xor
	call puts
	add esp, 4

	jmp task_done

task3:
	; TASK 3: XORing strings represented as hex strings

	; TODO TASK 1: find the addresses of both strings
	; TODO TASK 1: call the xor_hex_strings function
        
        push ecx
        call strlen
        pop ecx

        add eax, ecx
        inc eax
        
        mov edi, eax
        push edi
        
        push ecx                     
        call xor_hex_strings
        call puts
        add esp, 4

        jmp task_done

task4:
	; TASK 4: decoding a base32-encoded string

	; TODO TASK 4: call the base32decode function
	
	push ecx
	call puts                    ;print resulting string
	pop ecx
	
	jmp task_done

task5:
	; TASK 5: Find the single-byte key used in a XOR encoding

	; TODO TASK 5: call the bruteforce_singlebyte_xor function

	push ecx
        call bruteforce_singlebyte_xor                   ;print resulting string
	mov ebx, eax
        call puts
	pop ecx
        
        mov eax, ebx
	push eax                    ;eax = key value
	push fmtstr
	call printf                 ;print key value
	add esp, 8

	jmp task_done

task6:
	; TASK 6: decode Vignere cipher

	; TODO TASK 6: find the addresses for the input string and key
	; TODO TASK 6: call the decode_vigenere function

	push ecx
	call strlen
	pop ecx

	add eax, ecx
	inc eax

	push eax
	push ecx                   ;ecx = address of input string 
	call decode_vigenere
	pop ecx
	add esp, 4

	push ecx
	call puts
	add esp, 4

task_done:
	xor eax, eax
	jmp exit

exit_bad_arg:
	mov ebx, [ebp + 12]
	mov ecx , [ebx]
	push ecx
	push usage
	call printf
	add esp, 8
	jmp exit

exit_no_input:
	push filename
	push error_no_file
	call printf
	add esp, 8
	jmp exit

exit_cannot_read:
	push filename
	push error_cannot_read
	call printf
	add esp, 8
	jmp exit

exit:
	mov esp, ebp
	pop ebp
	ret